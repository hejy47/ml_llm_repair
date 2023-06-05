## Fixed Function 1
@Override
public void enterScope(NodeTraversal t) {
    if (t.inGlobalScope()) {
        // Don't even bother. All global variables are likely escaped.
        return;
    }
    if (LiveVariablesAnalysis.MAX_VARIABLES_TO_ANALYZE < t.getScope().getVarCount()) {
        return;
    }
    // Compute the forward reaching definition.
    ControlFlowAnalysis cfa = new ControlFlowAnalysis(compiler, false, true);
    // Process the body of the function.
    Preconditions.checkState(t.getScopeRoot().isFunction());
    Node functionBody = t.getScopeRoot().getLastChild();
    if (functionBody.getFirstChild() != null) { // Ignore empty functions
        cfa.process(null, functionBody);
        cfg = cfa.getCfg();
        reachingDef = new MustBeReachingVariableDef(cfg, t.getScope(), compiler);
        reachingDef.analyze();
        candidates = Lists.newLinkedList();
        // Using the forward reaching definition search to find all the inline
        // candidates
        new NodeTraversal(compiler, new GatherCandidates()).traverse(functionBody);
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
}

## Fixed Function 2
private boolean canInline() {
    // Cannot inline a parameter.
    if (getDefCfgNode().isFunction() || getDefCfgNode().isParamList()) {
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
    if (def == null) { // Definition was not found.
        return false;
    }

    // Check that the assignment isn't used as a R-value.
    if (def.isAssign() && !NodeUtil.isExpressionResultUsed(def.getParent())) {
        return false;
    }

    // Ensure that right-hand side of the definition doesn't have side effects.
    if (checkRightOf(def, getDefCfgNode(), SIDE_EFFECT_PREDICATE)) {
        return false;
    }

    // Ensure that left-hand side of the use doesn't have side effects.
    if (checkLeftOf(use, useCfgNode, SIDE_EFFECT_PREDICATE)) {
        return false;
    }

    // Ensure that the right-hand side of the definition doesn't have any
    // side effects anywhere in the program.
    if (NodeUtil.mayHaveSideEffects(def.getLastChild(), compiler)) {
        return false;
    }

    // Ensure that the name is not within a loop.
    if (NodeUtil.isWithinLoop(use)) {
        return false;
    }

    // Ensure that the variable is only used once.
    Collection<Node> uses = reachingUses.getUses(varName, getDefCfgNode());
    if (uses.size() != 1) {
        return false;
    }

    // Ensure that the definition and use nodes are not part of a finally block
    // as we cannot determine when the inlined code would be executed in this case.
    if (NodeUtil.isInFinallyBlock(getDefCfgNode()) || NodeUtil.isInFinallyBlock(useCfgNode)) {
        return false;
    }

    // We give up inlining stuff with R-values that have:
    // 1. GETPROP, GETELEM, or CALL nodes.
    // 2. Regexp, object literal, or array literal nodes.
    // 3. Direct reference to catch expressions.
    // Example:
    // var x = a.b.c; j.c = 1; print(x);
    // Inlining print(a.b.c) is not safe consider j and be alias to a.b.
    if (NodeUtil.has(def.getLastChild(), new Predicate<Node>() {

        @Override
        public boolean apply(Node input) {
            switch (input.getType()) {
                case Token.GETELEM:
                case Token.GETPROP:
                case Token.CALL:
                    return true;
                case Token.REGEXP:
                case Token.OBJECTLIT:
                case Token.ARRAYLIT:
                    // 1 is required because the ContainsReferencesOnlyPredicate skips empty nodes.
                    return NodeUtil.has(input.getLastChild(), Predicates.alwaysTrue(), Predicates.<Node>equalTo(input)) && input.getFirstChild().hasChildren();
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

    // Ensure that inlining twice does not increase code size.
    if (numUseWithinUseCfgNode != 1) {
        return false;
    }

    return true;
}