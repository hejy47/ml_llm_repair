## Fixed Function 1
@Override
public void process(Node externs, Node root) {
    NodeTraversal.traverse(compiler, root, new NodeTraversal.Callback() {
        @Override
        public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
            return true;
        }

        @Override
        public void visit(NodeTraversal t, Node n, Node parent) {
            // Code to be executed during the traversal
        }
    });
}


## Fixed Function 2
private void computeDependence(final Definition def, Node rValue) {
    NodeTraversal.traverse(compiler, rValue, new AbstractPreOrderCallback() {
        @Override
        public void visit(NodeTraversal t, Node n, Node parent) {
            if (n.isName() && jsScope.isDeclared(n.getString(), true)) {
                Var dep = jsScope.getVar(n.getString());
                def.depends.add(dep);
            }
        }
    });
}


## Fixed Function 3
boolean dependsOnOuterScopeVars(String name, Node useNode) {
    Preconditions.checkArgument(getCfg().hasNode(useNode));
    GraphNode<Node, Branch> n = getCfg().getNode(useNode);
    FlowState<MustDef> state = n.getAnnotation();
    Definition def = state.getIn().reachingDef.get(jsScope.getVar(name));
    for (Var s : def.depends) {
        if (s.getScopeRoot() != jsScope.getRootNode()) {
            return true;
        }
    }
    return false;
}