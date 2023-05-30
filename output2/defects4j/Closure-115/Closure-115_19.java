private CanInlineResult canInlineReferenceDirectly(Node callNode, Node fnNode) {
    if (!isDirectCallNodeReplacementPossible(fnNode)) {
        return CanInlineResult.NO;
    }
    Node block = fnNode.getLastChild();
    boolean hasSideEffects = false;
    if (block.hasChildren()) {
        Preconditions.checkState(block.hasOneChild());
        Node stmt = block.getFirstChild();
        if (stmt.isReturn()) {
            hasSideEffects = NodeUtil.mayHaveSideEffects(stmt.getFirstChild(), compiler);
        }
    }
    // CALL NODE: [ NAME, ARG1, ARG2, ... ]
    Node cArg = callNode.getFirstChild().getNext();
    // Functions called via 'call' and 'apply' have a this-object as
    // the first parameter, but this is not part of the called function's
    // parameter list.
    if (!callNode.getFirstChild().isName()) {
        if (NodeUtil.isFunctionObjectCall(callNode)) {
            // TODO(johnlenz): Support replace this with a value.
            if (cArg == null || !cArg.isThis()) {
                return CanInlineResult.NO;
            }
            cArg = cArg.getNext();
        } else {
            // ".apply" call should be filtered before this.
            Preconditions.checkState(!NodeUtil.isFunctionObjectApply(callNode));
        }
    }
    // FUNCTION NODE -> LP NODE: [ ARG1, ARG2, ... ]
    Node fnParam = NodeUtil.getFunctionParameters(fnNode).getFirstChild();
    while (cArg != null || fnParam != null) {
        // For each named parameter check if the corresponding argument has side-effects or if it is evaluated more than once.
        if (fnParam != null) {
            if (cArg != null) {
                if (hasSideEffects && NodeUtil.canBeSideEffected(cArg)) {
                    return CanInlineResult.NO;
                }
                // Check for arguments that are evaluated more than once.
                if (NodeUtil.mayEffectMutableState(cArg, compiler) && NodeUtil.getNameReferenceCount(block, fnParam.getString()) > 1) {
                    return CanInlineResult.NO;
                }
            } else {
                // If there are no more arguments but there are still named parameters, the function can't be inlined.
                return CanInlineResult.NO;
            }
            // Move to the next name and argument.
            cArg = cArg.getNext();
            fnParam = fnParam.getNext();
        } else {
            // If there are no more named parameters but there are still arguments, check if they have side-effects.
            if (cArg != null) {
                if (NodeUtil.mayHaveSideEffects(cArg, compiler)) {
                    return CanInlineResult.NO;
                }
                cArg = cArg.getNext();
            } else {
                // If there are no more named parameters nor arguments, the function can be inlined.
                break;
            }
        }
    }
    return CanInlineResult.YES;
}