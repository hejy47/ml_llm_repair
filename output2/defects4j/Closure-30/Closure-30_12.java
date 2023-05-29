## Fixed Function 1
@Override
public void process(Node externs, Node root) {
    NodeTraversal.traverse(compiler, root, new MyCallback());
}

private static class MyCallback extends NodeTraversal.Callback {
    @Override
    public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
        return true;
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
        // do something during traversal
    }
}

## Fixed Function 2
private void computeDependence(final Definition def, Node rValue) {
    NodeTraversal.traverse(compiler, rValue, new NodeTraversal.Callback() {
        @Override
        public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
            return true;
        }

        @Override
        public void visit(NodeTraversal t, Node n, Node parent) {
            JSScope scope = t.getScope();
            if (n.isName() && scope.isDeclared(n.getString(), true)) {
                Var dep = scope.getVar(n.getString());
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
        if (s.getScope() != jsScope) {
            return true;
        }
    }
    return false;
}