## Fixed Function 1
private boolean isSafeReplacement(Node node, Node replacement) {
    // No checks are needed for simple names.
    if (node.isName()) {
        return true;
    }
    Preconditions.checkArgument(node.isGetProp());
    Node propNameNode = node.getLastChild();
    if (propNameNode.isString() && isNameAssignedTo(propNameNode.getString(), replacement)) {
        return false;
    }
    return true;
}