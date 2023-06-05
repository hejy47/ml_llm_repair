## Fixed Function 1
private boolean hasExceptionHandler(Node cfgNode) {
    if (cfgNode.getCatchExceptions() != null) {
        return true;
    }
    if (cfgNode.getTryCatchBlocks() != null) {
        for (TryCatch tc : cfgNode.getTryCatchBlocks()) {
            if (tc.getCatchBlock() != null) {
                return true;
            }
        }
    }
    return false;
}