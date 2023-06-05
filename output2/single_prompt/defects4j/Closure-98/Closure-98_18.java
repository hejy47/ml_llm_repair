## Fixed Function 1
BasicBlock(BasicBlock parent, Node root) {
    this.parent = parent;
    // only named functions may be hoisted.
    // check if node is a function declaration or a named function expression
    this.isHoisted = (NodeUtil.isFunctionDeclaration(root) && NodeUtil.getFunctionName(root) != null)
                    || (NodeUtil.isFunctionExpression(root) && NodeUtil.getFunctionName(root) != null);
}

## Fixed Function 2
boolean isAssignedOnceInLifetime() {
    Reference ref = getOneAndOnlyAssignment();
    if (ref == null) {
        return false;
    }
    // Make sure this assignment is not in a loop.
    // check if reference is inside a loop
    return !NodeUtil.isWithinLoop(ref.getGrandparent());
}