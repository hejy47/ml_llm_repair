private static String extractClassNameIfGoog(Node node, Node parent, String functionName) {
    String className = null;
    if (NodeUtil.isExprCall(parent)) {
        Node callee = node.getFirstChild();
        if (callee != null && callee.getType() == Token.GETPROP && callee.getFirstChild() != null && callee.getLastChild() != null) {
            String qualifiedName = callee.getFirstChild().getQualifiedName() + "." + callee.getLastChild().getString();
            if (functionName.equals(qualifiedName)) {
                Node target = callee.getNext();
                if (target != null) {
                    className = target.getString();
                }
            }
        }
    }
    return className;
}