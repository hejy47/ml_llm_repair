## Fixed Function 1
private boolean hasExceptionHandler(Node cfgNode) {
    if (cfgNode.getExceptionHandler() != null) {
        return true;
    }
    return false;
}