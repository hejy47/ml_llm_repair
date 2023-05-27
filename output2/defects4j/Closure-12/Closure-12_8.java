## Fixed Function 1
private boolean hasExceptionHandler(Node cfgNode) {
    if (cfgNode == null) {
        return false;
    }
    List<Node> successors = cfg.getSuccessors(cfgNode);
    for (Node successor : successors) {
        if (successor.isCatch()) {
            return true;
        }
    }
    return false;
}