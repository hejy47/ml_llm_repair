## Fixed Function 1
boolean isAssignedOnceInLifetime() {
    Reference ref = getOneAndOnlyAssignment();
    if (ref == null) {
        return false;
    }
    // Make sure this assignment is not in a loop.
    BasicBlock block = ref.getBasicBlock();
    while (block != null) {
        if (block.isLoop) {
            return false;
        } else if (block.isFunction) {
            break;
        } else {
            block = block.getParent();
        }
    }
    return true;
}