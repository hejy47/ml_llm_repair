## Fixed Function 1
private boolean isSafeReplacement(Node node, Node replacement) {
    // No checks are needed for simple names.
    if (node.isName()) {
        return true;
    }
    Preconditions.checkArgument(node.isGetProp());
    node = node.getFirstChild();
    while (node != null) {
        if (node.isName() && isNameAssignedTo(node.getString(), replacement)) {
            return false;
        }
        node = node.getNext();
    }
    return true;
}