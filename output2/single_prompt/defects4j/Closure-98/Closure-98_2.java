## Fixed Function 1
BasicBlock(BasicBlock parent, Node root) {
    this.parent = parent;
    // only named functions may be hoisted.
    this.isHoisted = NodeUtil.isHoistedFunctionDeclaration(root.getParent());
}

## Fixed Function 2
boolean isAssignedOnceInLifetime() {
    Reference ref = getOneAndOnlyAssignment();
    if (ref == null) {
        return false;
    }
    // Make sure this assignment is not in a loop.
    Node node = ref.getNode();
    boolean isLoop = ControlFlowGraph.isEnteringNewFunction(node);
    return !isLoop;
}