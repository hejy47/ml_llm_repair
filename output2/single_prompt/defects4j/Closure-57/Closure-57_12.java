private static String extractClassNameIfGoog(Node node, Node parent, String functionName) {
    String className = null;
    if (NodeUtil.isExprCall(parent)) {
        Node callee = node.getFirstChild();
        if (callee != null && callee.getType() == Token.GETPROP) {
            String qualifiedName = callee.getQualifiedName();
            if (qualifiedName != null && qualifiedName.endsWith("." + functionName)) { // check for qualified name and end with functionName
                Node target = callee.getNext();
                if (target != null) {
                    className = target.getString();
                }
            }
        }
    }
    return className;
}