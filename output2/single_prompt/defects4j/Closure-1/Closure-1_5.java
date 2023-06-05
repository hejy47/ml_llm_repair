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
    Preconditions.checkState(NodeUtil.isFunctionExpression(function) || NodeUtil.isArrowFunctionExpression(function)
        || NodeUtil.isFunctionDeclaration(function));
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
            if (lastArg.isDefaultValue() || lastArg.isRest()) {
                // Unreferenced default parameters and rest parameters can't be stripped.
                break;
            }
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
}