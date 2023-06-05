## Fixed Function 1
private boolean hasExceptionHandler(Node cfgNode) {
    if(cfgNode.getCatchBlocks() != null && !cfgNode.getCatchBlocks().isEmpty()) {
        return true;
    }
    return false;
}