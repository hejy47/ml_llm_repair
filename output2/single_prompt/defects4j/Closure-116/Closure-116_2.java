private CanInlineResult canInlineReferenceDirectly(Node callNode, Node fnNode) {
    if (!isDirectCallNodeReplacementPossible(fnNode)) {
        return CanInlineResult.NO;
    }
    Node block = fnNode.getLastChild();
    // CALL NODE: [ NAME, ARG1, ARG2, ... ]
    
    // Check if the call node is a direct call to a function,
    // since indirect calls cannot be inlined directly.
    if (!callNode.getFirstChild().isName()) {
        return CanInlineResult.NO;
    }

    Node cArg = callNode.getFirstChild().getNext();
    
    // FUNCTION NODE -> LP NODE: [ ARG1, ARG2, ... ]
    Node fnParam = NodeUtil.getFunctionParameters(fnNode).getFirstChild();
    while (cArg != null || fnParam != null) {
        // For each named parameter check if a mutable argument use more than one.
        if (fnParam != null) {
            if (cArg != null) {
                // Check for arguments that are evaluated more than once.
                // Note: Unlike block inlining, there it is not possible that a
                // parameter reference will be in a loop.
                if (NodeUtil.mayEffectMutableState(cArg, compiler) && NodeUtil.getNameReferenceCount(block, fnParam.getString()) > 1) {
                    return CanInlineResult.NO;
                }
            }
            // Move to the next name.
            fnParam = fnParam.getNext();
        }
        // For every call argument check for side-effects, even if there
        // isn't a named parameter to match.
        if (cArg != null) {
            if (NodeUtil.mayHaveSideEffects(cArg, compiler)) {
                return CanInlineResult.NO;
            }
            cArg = cArg.getNext();
        }
    }
    return CanInlineResult.YES;
}