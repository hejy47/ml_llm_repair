## Fixed Function 1
BasicBlock(BasicBlock parent, Node root) {
    this.parent = parent;
    // only named functions may be hoisted.
    // Add a null check to avoid NullPointerException
    this.isHoisted = (root != null && NodeUtil.isHoistedFunctionDeclaration(root));
}

## Fixed Function 2
boolean isAssignedOnceInLifetime() {
    Reference ref = getOneAndOnlyAssignment();
    if (ref == null) {
        return false;
    }
    // Make sure this assignment is not in a loop.
    // Add a check to see if the reference appears in a loop
    return NodeUtil.getEnclosingLoop(ref.getDeclaration()) == null;
}