## Fixed Function 1
private boolean hasExceptionHandler(Node cfgNode) {
    // Check if any exception handlers exist for this node in the CFG
    for (ExceptionHandler handler : cfgNode.getExceptionHandlers()) {
        return true;
    }
    return false;
}