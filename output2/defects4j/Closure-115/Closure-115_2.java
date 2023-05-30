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
    Node cArg = callNode.getFirstChild().getNext();
    if (!callNode.getFirstChild().isName()) {
        if (NodeUtil.isFunctionObjectCall(callNode)) {
            if (cArg == null || !cArg.isThis()) {
                return CanInlineResult.NO;
            }
            cArg = cArg.getNext();
        } else {
            Preconditions.checkState(!NodeUtil.isFunctionObjectApply(callNode));
        }
    }
    Node fnParam = NodeUtil.getFunctionParameters(fnNode).getFirstChild();
    while (cArg != null || fnParam != null) {
        if (fnParam != null) {
            if (cArg == null) { // Fixed
                return CanInlineResult.NO;
            }
            if (hasSideEffects && NodeUtil.canBeSideEffected(cArg)) {
                return CanInlineResult.NO;
            }
            if (NodeUtil.mayEffectMutableState(cArg, compiler) && NodeUtil.getNameReferenceCount(block, fnParam.getString()) > 1) {
                return CanInlineResult.NO;
            }
            fnParam = fnParam.getNext();
        }
        if (cArg != null) {
            if (NodeUtil.mayHaveSideEffects(cArg, compiler)) {
                return CanInlineResult.NO;
            }
            cArg = cArg.getNext();
        }
    }
    return CanInlineResult.YES;
}