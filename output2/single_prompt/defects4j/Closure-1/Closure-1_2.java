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
        // fix: check if the function has properties that require its original length
        if (NodeUtil.isFunctionDeclaration(function)) {
          if (function.getJSDocInfo() != null) {
            ImmutableList<JSTypeExpression> params = function.getJSDocInfo().getParameterTypeExpressions();
            int expectedParamCount = params == null ? 0 : params.size();
            if (argList.getChildCount() != expectedParamCount) {
              JSDocInfoBuilder info = JSDocInfoBuilder.copyFrom(function.getJSDocInfo());
              info.recordParameterLength(argList.getChildCount());
              function.setJSDocInfo(info.build());
            }
          }
        }
    } else {
        callSiteOptimizer.optimize(fnScope, referenced);
    }
}