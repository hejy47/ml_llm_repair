## Fixed Function 1
@Override
public void process(Node externs, Node root) {
    NodeTraversal.traverse(compiler, root, new MyTraversal(compiler, this));
}

class MyTraversal extends AbstractPostOrderCallback implements NodeTraversal.Callback {
    private final AbstractCompiler compiler;
    private final MyPass myPass;

    public MyTraversal(AbstractCompiler compiler, MyPass myPass) {
        this.compiler = compiler;
        this.myPass = myPass;
    }

    @Override
    public void visit(NodeTraversal t, Node n, Node parent) {
        // do something
    }
}

## Fixed Function 2
private void computeDependence(final Definition def, Node rValue) {
    NodeTraversal.traverse(compiler, rValue, new AbstractPostOrderCallback() {

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
    GraphNode<Node, Branch> n = getCfg().getNodeFor(useNode);
    FlowState<MustDef> state = n.getAnnotation();
    Definition def = state.getIn().getReachingDef(jsScope.getVar(name));
    for (Var s : def.depends) {
        if (s.scope != jsScope) {
            return true;
        }
    }
    return false;
}