private static String extractClassNameIfGoog(Node node, Node parent, String functionName) {
    String className = null;
    if (NodeUtil.isExprCall(parent)) {
        Node callee = node.getFirstChild();
        if (callee != null && callee.getType() == Token.GETPROP) {
            Node target = callee.getLastChild();
            String qualifiedName = callee.getFirstChild().getQualifiedName();
            if (functionName.equals(qualifiedName) && target != null && target.isString()) {
                className = target.getString();
            }
        }
    }
    return className;
}