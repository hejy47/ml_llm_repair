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
        Node lastArg = argList.getLastChild();
        while (lastArg != null && !referenced.contains(fnScope.getVar(lastArg.getString()))) {
            argList.removeChild(lastArg);
            compiler.reportCodeChange();
            lastArg = argList.getLastChild();
        }
    } else {
        callSiteOptimizer.optimize(fnScope, referenced);
    }
}