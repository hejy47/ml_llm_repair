## Fixed Function 1
boolean isAssignedOnceInLifetime() {
    Reference ref = getOneAndOnlyAssignment();
    if (ref == null) {
        return false;
    }
    // Make sure this assignment is not in a loop.
    for (BasicBlock block = ref.getBasicBlock(); block != null; block = block.getParent()) {
        if (block.isFunction) {
            return true; // fixed line
        } else if (block.isLoop) {
            return false;
        }
    }
    return false; // fixed line
}