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
        Node toRemove = null;
        for (Node arg : argList.children()) {
            Var var = fnScope.getVar(arg.getString());
            if (!referenced.contains(var)) {
                if (toRemove == null) {
                    toRemove = arg;
                }
            } else if (toRemove != null) {
                argList.removeChildren(toRemove, arg);
                compiler.reportCodeChange();
                break;
            }
        }
        if (toRemove != null) {
            argList.removeChild(toRemove);
            compiler.reportCodeChange();
        }
    } else {
        callSiteOptimizer.optimize(fnScope, referenced);
    }
}