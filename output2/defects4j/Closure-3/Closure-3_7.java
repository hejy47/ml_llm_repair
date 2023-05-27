## Fixed Function 1
@Override
public void enterScope(NodeTraversal t) {
    if (t.inGlobalScope()) {
        // Don't even brother. All global variables are likely escaped.
        return;
    }
    if (LiveVariablesAnalysis.MAX_VARIABLES_TO_ANALYZE < t.getScope().getVarCount()) {
        return;
    }
    // Compute the forward reaching definition.
    ControlFlowAnalysis cfa = new ControlFlowAnalysis(compiler, false, true);
    // Process the body of the function.
    Preconditions.checkState(t.getScopeRoot().isFunction());
    cfa.process(null, t.getScopeRoot().getLastChild());
    cfg = cfa.getCfg();
    reachingDef = new MustBeReachingVariableDef(cfg, t.getScope(), compiler);
    reachingDef.analyze();
    candidates = Lists.newLinkedList();
    // Using the forward reaching definition search to find all the inline
    // candidates
    new NodeTraversal(compiler, new GatherCandidates()).traverse(t.getScopeRoot().getLastChild());
    // Compute the backward reaching use. The CFG can be reused.
    reachingUses = new MaybeReachingVariableUse(cfg, t.getScope(), compiler);
    reachingUses.analyze();
    for (Candidate c : candidates) {
        if (c.canInline()) {
            c.inlineVariable();
            // If definition c has dependencies, then inlining it may have
            // introduced new dependencies for our other inlining candidates.
            //
            // MustBeReachingVariableDef uses this dependency graph in its
            // analysis, so some of these candidates may no longer be valid.
            // We keep track of when the variable dependency graph changed
            // so that we can back off appropriately.
            if (!c.defMetadata.depends.isEmpty()) {
                inlinedNewDependencies.add(t.getScope().getVar(c.varName));
            }
        }
    }
}

## Fixed Function 2
private boolean canInline() {
    // Cannot inline a parameter.
    if (getDefCfgNode().isFunction() || getDefCfgNode().isCatch()) {
        return false;
    }
    // If one of our dependencies has been inlined, then our dependency
    // graph is wrong. Re-computing it would take another CFG computation,
    // so we just back off for now.
    for (Var dependency : defMetadata.depends) {
        if (inlinedNewDependencies.contains(dependency)) {
            return false;
        }
    }
    getDefinition(getDefCfgNode(), null);
    getNumUseInUseCfgNode(useCfgNode, null);
    // Definition was not found.
    if (def == null) {
        return false;
    }
    // Check that the assignment isn't used as a R-Value.
    // TODO(user): Certain cases we can still inline.
    if (def.isAssign() && !NodeUtil.isExpressionAssign(def.getParent())) {
        return false;
    }
    // The right of the definition has side effect:
    // Example, for x:
    // x = readProp(b), modifyProp(b); print(x);
    if (checkRightOf(def, getDefCfgNode(), SIDE_EFFECT_PREDICATE, useCfgNode)) {
        return false;
    }
    // Similar check as the above but this time, all the sub-expressions
    // left of the use of the variable.
    // x = readProp(b); modifyProp(b), print(x);
    if (checkLeftOf(use, useCfgNode, SIDE_EFFECT_PREDICATE, getDefCfgNode())) {
        return false;
    }
    // TODO(user): Side-effect is OK sometimes. As long as there are no
    // side-effect function down all paths to the use. Once we have all the
    // side-effect analysis tool.
    if (NodeTraversal.hasSideEffect(
            def.getLastChild(), compiler, NodeUtil.SideEffectOptions.MAY_HAVE_SIDE_EFFECTS)) {
        return false;
    }
    // TODO:user): We could inline all the uses if the expression is short.
    // Finally we have to make sure that there are no more than one use
    // in the program and in the CFG node. Even when it is semantically
    // correctly inlining twice increases code size.
    if (numUseWithinUseCfgNode != 1 || reachingUses.getUses(varName, getDefCfgNode()).size() != 1) {
        return false;
    }
    // Make sure that the name is not within a loop.
    if (NodeUtil.isWithinLoop(use)) {
        return false;
    }
    // We give up inlining stuff with R-Value that has:
    // 1) GETPROP, GETELEM,
    // 2) anything that creates a new object.
    // 3) a direct reference to a catch expression.
    // Example:
    // var x = a.b.c; j.c = 1; print(x);
    // Inlining print(a.b.c) is not safe consider j and be alias to a.b.
    // TODO(user): We could get more accuracy by looking more in-detail
    // what j is and what x is trying to into to.
    // TODO(johnlenz): rework catch expression handling when we
    // have lexical scope support so catch expressions don't
    // need to be special cased.
    if (NodeUtil.has(def.getLastChild(), new Predicate<Node>() {

        @Override
        public boolean apply(Node input) {
            switch (input.getType()) {
                case Token.GETELEM:
                case Token.GETPROP:
                case Token.ARRAYLIT:
                case Token.OBJECTLIT:
                case Token.REGEXP:
                case Token.NEW:
                    return true;
            }
            return false;
        }
    }, new Predicate<Node>() {

        @Override
        public boolean apply(Node input) {
            // Recurse if the node is not a function.
            return !input.isFunction();
        }
    })) {
        return false;
    }
    // We can skip the side effect check along the paths of two nodes if they are just next to each
    // other.
    // Similar side effect check as above but this time the side effect is else where along the path.
    // x = readProp(b); while(modifyProp(b)) {}; print(x);
    if (!NodeUtil.isStatementBlock(getDefCfgNode().getParent())) {
        return true;
    }
    ControlFlowGraph<Node> graph = reachingUses.getCfg();
    if (getDefCfgNode().getNext() == useCfgNode) {
        return true;
    }
    Stack<TraversalPath<Node, ControlFlowGraph.Branch>> stack =
            new Stack<TraversalPath<Node, ControlFlowGraph.Branch>>();
    ControlFlowGraph.Branch root = new ControlFlowGraph.Branch() {};
    stack.push(new TraversalPath<Node, ControlFlowGraph.Branch>(
            graph.getDirectedGraphNode(getDefCfgNode()), root));
    while (!stack.isEmpty()) {
        TraversalPath<Node, ControlFlowGraph.Branch> currentPath = stack.pop();
        String message = currentPath.predicateOutput;
        ControlFlowGraph.Branch currentBranching = currentPath.endBranch;
        ControlFlowGraph.Branch startingBranch = (currentPath.getPathLength() == 1) ? root :
                currentPath.get(currentPath.getPathLength() - 2).getPayload();
        if (!CheckSideEffectLoop.containsStatement(
                startingBranch, currentBranching, SIDE_EFFECT_PREDICATE, message)) {
            continue;
        }
        if (currentPath.end().equals(graph.getDirectedGraphNode(useCfgNode))) {
            return false;
        }
        stack.addAll(currentPath.extend(SIDE_EFFECT_PREDICATE));
    }
    return true;
}