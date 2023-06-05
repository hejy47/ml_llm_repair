## Fixed Function 1
BasicBlock(BasicBlock parent, Node root) {
    this.parent = parent;
    // only named functions may be hoisted.
    this.isHoisted = false; // set isHoisted to false by default
    if (root.isFunction()) {
        this.isHoisted = NodeUtil.isHoistedFunctionDeclaration(root);
    }
}

## Fixed Function 2
boolean isAssignedOnceInLifetime() {
    Reference ref = getOneAndOnlyAssignment();
    if (ref == null) {
        return false;
    }
    // Make sure this assignment is not in a loop.
    Node node = ref.getGrandparent();
    while (node != null && !node.isFunction()) {
        if (node.isLoop() || node.isFor()) {
            return false;
        }
        node = node.getParent();
    }
    return true;
}