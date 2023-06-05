## Fixed Function 1
BasicBlock(BasicBlock parent, Node root) {
    this.parent = parent;
    // only named functions may be hoisted.
    this.isHoisted = NodeUtil.isHoistedFunctionDeclaration(root) && NodeUtil.isNameDeclaration(root);
}

## Fixed Function 2
boolean isAssignedOnceInLifetime() {
    Reference ref = getOneAndOnlyAssignment();
    if (ref == null) {
        return false;
    }
    // Make sure this assignment is not in a loop.
    Node node = ref.getNode();
    while (node != null) {
        if (NodeUtil.isLoopStructure(node)) {
            return false;
        }
        node = node.getParent();
    }
    return true;
}