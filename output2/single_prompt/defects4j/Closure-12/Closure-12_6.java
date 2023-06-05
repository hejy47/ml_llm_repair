## Fixed Function 1
private boolean hasExceptionHandler(Node cfgNode) {
    return cfgNode.getCatchHandlers().size() > 0 || cfgNode.getFinally() != null;
}