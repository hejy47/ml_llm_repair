private void removeUnreferencedFunctionArgs(Scope fnScope) {
    Node function = fnScope.getRootNode();
    Preconditions.checkState(function.isFunction());
    if (NodeUtil.isGetOrSetKey(function.getParent())) {
        // The parameters object literal setters can not be removed.
        return;
    }
    Node argList = getFunctionArgList(function);
    // Create a list of referenced argument names
    Set<String> referencedArgs = new HashSet<>();
    for (Var var : fnScope.getVarIterable()) {
        if (var.isParam() && referenced.contains(var)) {
            referencedArgs.add(var.getName());
        }
    }
    boolean modifyCallers = modifyCallSites && callSiteOptimizer.canModifyCallers(function);
    if (!modifyCallers) {
        // Strip unreferenced args off the end of the function declaration.
        Node lastArg;
        while ((lastArg = argList.getLastChild()) != null) {
            String argName = lastArg.getString();
            if (!referencedArgs.contains(argName)) {
                argList.removeChild(lastArg);
                compiler.reportCodeChange();
            } else {
                break;
            }
        }
    } else {
        callSiteOptimizer.optimize(fnScope, referenced);
    }
}