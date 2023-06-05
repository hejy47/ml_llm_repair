## Fixed Function 1
private boolean hasExceptionHandler(Node cfgNode) {
    return !cfgNode.getCatchBlocks().isEmpty();
}