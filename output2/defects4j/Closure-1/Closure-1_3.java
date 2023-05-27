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
        while (lastArg != null) {
            Node prev = lastArg.getPrevious();
            Var var = fnScope.getVar(lastArg.getString());
            if (!referenced.contains(var)) {
                argList.removeChild(lastArg);
                compiler.reportCodeChange();
            } else if (prev != null) {
                // If we didn't remove the last argument, check if its predecessor is unreferenced.
                // If so, remove it and iterate again.
                Var prevVar = fnScope.getVar(prev.getString());
                if (!referenced.contains(prevVar)) {
                    argList.removeChild(prev);
                    compiler.reportCodeChange();
                    continue;
                }
            }
            lastArg = prev;
        }
    } else {
        callSiteOptimizer.optimize(fnScope, referenced);
    }
}