private static String extractClassNameIfGoog(Node node, Node parent, String functionName) {
    String className = null;
    //added null check for parent
    if (parent != null && NodeUtil.isExprCall(parent)) {
        Node callee = node.getFirstChild();
        if (callee != null && callee.getType() == Token.GETPROP) {
            String qualifiedName = callee.getQualifiedName();
            //changed .equals to .startsWith to handle cases where functionName is a qualified name with a namespace
            if (qualifiedName != null && qualifiedName.startsWith(functionName)) {
                Node target = callee.getNext();
                if (target != null) {
                    className = target.getString();
                }
            }
        }
    }
    return className;
}