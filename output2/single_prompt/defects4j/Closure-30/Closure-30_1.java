## Fixed Function 1
@Override
public void process(Node externs, Node root) {
    NodeTraversal.traverse(compiler, root, new MyProcessCallback());
}

private class MyProcessCallback extends AbstractPostOrderCallback {
    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
        // Your processing here
    }
}


## Fixed Function 2
private void computeDependence(final Definition def, Node rValue) {
    NodeTraversal.traverse(compiler, rValue, new AbstractShallowStatementCallback() {

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
    if (state != null) {
        Definition def = state.getIn().reachingDef.get(jsScope.getVar(name));
        if (def != null) {
            for (Var s : def.depends) {
                if (s.scope != jsScope) {
                    return true;
                }
            }
        }
    }
    return false;
}