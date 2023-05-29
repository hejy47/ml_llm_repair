private static String extractClassNameIfGoog(Node node, Node parent, String functionName) {
    String className = null;
    if (NodeUtil.isExprCall(parent)) {
        Node callee = node.getFirstChild();
        if (callee != null && callee.getType() == Token.GETPROP) {
            String qualifiedName = callee.getQualifiedName();
            if (qualifiedName != null && functionName.equals(qualifiedName)) { // Check if qualifiedName is not null
                Node target = callee.getNext();
                if (target != null && target.getType() == Token.STRING) { // Check if target is a string node
                    className = target.getString();
                }
            }
        }
    }
    return className;
}