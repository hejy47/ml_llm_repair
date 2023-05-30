## Fixed Function 1
BasicBlock(BasicBlock parent, Node root) {
    this.parent = parent;
    // only named functions may be hoisted.
    this.isHoisted = NodeUtil.isHoistedFunctionDeclaration(root.getFirstChild());
}

## Fixed Function 2
boolean isAssignedOnceInLifetime() {
    Reference ref = getOneAndOnlyAssignment();
    if (ref == null) {
        return false;
    }
    // Make sure this assignment is not in a loop.
    Node currentNode = ref.getNode();
    while (currentNode.getParent() != null) {
        currentNode = currentNode.getParent();
        if (currentNode.isLoop()) {
            return false;
        }
    }
    return true;
}