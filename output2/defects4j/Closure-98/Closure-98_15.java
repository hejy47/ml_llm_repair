## Fixed Function 1
BasicBlock(BasicBlock parent, Node root) {
    this.parent = parent;
    // check if root is a function declaration before checking for hoisting
    this.isHoisted = root.isFunction() && NodeUtil.isHoistedFunctionDeclaration(root);
}

## Fixed Function 2
boolean isAssignedOnceInLifetime() {
    Reference ref = getOneAndOnlyAssignment();
    if (ref == null) {
        return false;
    }
    // Make sure this assignment is not in a loop.
    Node enclosingLoop = NodeUtil.getEnclosingIterationNode(ref.getDeclarationNode());
    return enclosingLoop == null;
}