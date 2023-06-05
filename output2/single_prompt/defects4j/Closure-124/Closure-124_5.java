## Fixed Function 1
private boolean isSafeReplacement(Node node, Node replacement) {
    // No checks are needed for simple names.
    if (node.isName()) {
        return true;
    }
    
    if (node.isGetProp()) {
        node = node.getFirstChild();
        if (node.isName() && isNameAssignedTo(node.getString(), replacement)) {
            return false;
        }
    }
    return true;
}