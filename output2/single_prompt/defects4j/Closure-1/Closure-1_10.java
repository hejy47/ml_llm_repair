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
    if (!modifyCallers) {
        // Strip unreferenced args off the end of the function declaration.
        Node lastArg;
        while ((lastArg = argList.getLastChild()) != null) {
            if (lastArg.isName()) {
                Var var = fnScope.getVar(lastArg.getString());
                if (!referenced.contains(var)) {
                    argList.removeChild(lastArg);
                    compiler.reportCodeChange();
                } else {
                    break;
                }
            } else { 
                // If we have a destructured argument we check each destructured name in the argument
                for (Node child = lastArg.getFirstChild(); child != null; child = child.getNext()) {
                    if (child.isName()) {
                        Var var = fnScope.getVar(child.getString());
                        if (!referenced.contains(var)) {
                            child.detachFromParent();
                            compiler.reportCodeChange();
                        }
                    }
                }
                break;
            }
        }
    } else {
        callSiteOptimizer.optimize(fnScope, referenced);
    }
}