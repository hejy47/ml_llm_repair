## Fixed Function 1
private boolean hasExceptionHandler(Node cfgNode) {
    List<Node> successors = cfgNode.getSuccessors();
    for (Node successor : successors) {
        if (successor.getType().equals(Node.Type.EXCEPTION_HANDLER)) {
            return true;
        }
    }
    return false;
}