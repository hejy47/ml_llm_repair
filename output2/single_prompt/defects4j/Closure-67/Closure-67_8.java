private boolean isPrototypePropertyAssign(Node assign) {
    Node n = assign.getFirstChild();
    if (n != null && NodeUtil.isVarOrSimpleAssignLhs(n, assign) && n.getType() == Token.GETPROP) {
        // We want to exclude the assignment itself from the usage list
        boolean isChainedProperty = n.getFirstChild().getType() == Token.GETPROP;
        // Check that the property chain is not too long before checking for "prototype"
        if (isChainedProperty && n.getFirstChild().getChildCount() == 2) {
            Node child = n.getFirstChild().getLastChild();
            if (child.getType() == Token.STRING && child.getString().equals("prototype")) {
                return true;
            }
        }
    }
    return false;
}