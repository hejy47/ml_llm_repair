## Fixed Function 1
@Override
public void process(Node externs, Node root) {
    NodeTraversal.traverse(compiler, root, new MyTraversalCallback());
}

private static class MyTraversalCallback extends AbstractPostOrderCallback {
    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
         // Do something with the node
    }
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
    Preconditions.checkNotNull(getCfg());
    Preconditions.checkNotNull(useNode);

    GraphNode<Node, Branch> n = getCfg().getNode(useNode);
    FlowState<MustDef> state = n.getAnnotation();
    if (state != null) {
        Definition def = state.getIn().reachingDef.get(jsScope.getVar(name));
        if (def != null && def.depends != null) {
            for (Var s : def.depends) {
                if (s.scope != jsScope) {
                    return true;
                }
            }
        }
    }

    return false;
}