private void removeUnreferencedFunctionArgs(Scope fnScope) {
    Node function = fnScope.getRootNode();
    Preconditions.checkState(function.isFunction());
    if (NodeUtil.isGetOrSetKey(function.getParent())) {
        // The parameters object literal setters can not be removed.
        return;
    }
    Node argList = getFunctionArgList(function);
    boolean modifyCallers = modifyCallSites && callSiteOptimizer.canModifyCallers(function);
    if (!modifyCallers) {
        // Strip unreferenced args off the end of the function declaration.
        Node lastArg;
        while ((lastArg = argList.getLastChild()) != null) {
            Var var = fnScope.getVar(lastArg.getString());
            if (!referenced.contains(var)) {
                argList.removeChild(lastArg);
                compiler.reportCodeChange();
            } else {
                break;
            }
        }
    } else {
        callSiteOptimizer.optimize(fnScope, referenced);
    }
    // Check the function's parameter list to make sure there are no unused function args.
    NodeUtil.forEachChild(argList, new NodeUtil.MatchShallowStatement() {
        @Override
        public void visit(NodeTraversal t, Node n, Node parent) {
            if (!n.isName()) {
                return;
            }
            Var var = t.getScope().getVar(n.getString());
            if (!referenced.contains(var)) {
                n.getParent().removeChild(n);
                compiler.reportCodeChange();
            }
        }
    });
}