## Fixed Function 1
boolean isAssignedOnceInLifetime() {
    Reference ref = getOneAndOnlyAssignment();
    if (ref == null) {
        return false;
    }
    // Make sure this assignment is not in a loop.
    for (BasicBlock block = ref.getBasicBlock(); block != null; block = block.getParent()) {
        if (block.isFunction()) { // add braces after function call
            break;
        } else if (block.isLoop()) { // add braces after function call
            return false;
        }
    }
    return true;
}