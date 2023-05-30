public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
    if (n.getType() == Token.FUNCTION) {
        // Don't traverse functions that are constructors or have the @this
        // or @override annotation.
        JSDocInfo jsDoc = getFunctionJsDocInfo(n);
        if (jsDoc != null && (jsDoc.isConstructor() || jsDoc.isInterface() || jsDoc.hasThisType() || jsDoc.isOverride())) {
            return false;
        }
        // Don't traverse functions unless they would normally
        // be able to have a @this annotation associated with them.
        int pType = parent.getType();
        if (!(pType == Token.BLOCK || pType == Token.SCRIPT || pType == Token.NAME || pType == Token.ASSIGN ||
                pType == Token.STRING || pType == Token.NUMBER ||
                pType == Token.MEMBER_FUNCTION_DEF || pType == Token.MEMBER_VAR_DEF)) {
            return false;
        }
        // Don't traverse functions that are getting lent to a prototype.
        if (pType == Token.ASSIGN) {
            Node lhs = parent.getFirstChild();
            Node rhs = lhs.getNext();
            if (rhs == null || NodeUtil.isFunctionExpression(rhs)) {
                return false;
            }
            if (NodeUtil.isGet(lhs) && lhs.getType() == Token.GETPROP && lhs.getLastChild().getString().equals("prototype")) {
                return false;
            }
        }
    }
    return true;
}