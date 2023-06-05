## Fixed Function 1
private boolean hasExceptionHandler(Node cfgNode) {
    if(cfgNode.getCatch() != null) {
        return true;
    }
    return false;
}