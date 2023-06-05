private void removeUnreferencedFunctionArgs(Scope fnScope) {
    // Notice that removing unreferenced function args breaks
    // Function.prototype.length. In advanced mode, we don't really care
    // about this: we consider "length" the equivalent of reflecting on
    // the function's lexical source.
    //
    // Rather than create a new option for this, we assume that if the user
    // is removing globals, then it's OK to remove unused function args.
    //
    // See http://code.google.com/p/closure-compiler/issues/detail?id=253
    Node function = fnScope.getRootNode();
    Preconditions.checkState(function.isFunction());
    if (NodeUtil.isGetOrSetKey(function.getParent())) {
        // The parameters object literal setters can not be removed.
        return;
    }
    Node argList = getFunctionArgList(function);
    boolean modifyCallers = modifyCallSites && callSiteOptimizer.canModifyCallers(function);
    List<Node> toRemove = new ArrayList<>();
    // Collect unreferenced args to remove.
    for (Node arg : argList.children()) {
        Var var = fnScope.getVar(arg.getString());
        if (!referenced.contains(var)) {
            toRemove.add(arg);
        } else if (!toRemove.isEmpty()) {
            // Stop at the first referenced arg after unreferenced args.
            break;
        }
    }
    // Remove collected args.
    for (Node arg : toRemove) {
        argList.removeChild(arg);
        compiler.reportCodeChange();
    }
    if (modifyCallers) {
        callSiteOptimizer.optimize(fnScope, referenced);
    }
}