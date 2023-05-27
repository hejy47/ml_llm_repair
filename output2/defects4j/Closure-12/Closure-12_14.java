## Fixed Function 1
private boolean hasExceptionHandler(Node cfgNode) {
    // Check if the node has an exception handler
    if(cfgNode.getCatchNodes().size() > 0){
        return true;
    }
    return false;
}