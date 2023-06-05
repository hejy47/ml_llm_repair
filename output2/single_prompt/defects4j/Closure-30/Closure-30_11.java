## Fixed Function 1
@Override
public void process(Node externs, Node root) {
    NodeTraversal.traverse(compiler, root, new MyNodeTraversal(compiler, this));
}

## Fixed Function 2
private void computeDependence(final Definition def, Node rValue) {
    NodeTraversal.traverse(compiler, rValue, new AbstractShallowCallback() {

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
    Preconditions.checkArgument(getCfg().getNode(useNode) != null);
    GraphNode<Node, Branch> n = getCfg().getNode(useNode);
    FlowState<MustDef> state = n.getAnnotation();
    Definition def = state.getIn().getReachingDef(jsScope.getVar(name));
    for (Var s : def.depends) {
        if (s.getScope() != jsScope) {
            return true;
        }
    }
    return false;
}