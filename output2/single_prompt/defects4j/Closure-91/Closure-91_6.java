public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {

    if (n.getType() == Token.FUNCTION) {
        // Don't traverse functions that are constructors or have the @this
        // or @override annotation.
        JSDocInfo jsDoc = getFunctionJsDocInfo(n);
        if (jsDoc != null && (jsDoc.isConstructor() || jsDoc.isInterface() || jsDoc.hasThisType() || jsDoc.isOverride())) {
            return false;
        }
        // Only traverse functions unless they would normally
        // be able to have a @this annotation associated with them.
        // Only traverse functions that are getting lent to a prototype.
        int pType = parent.getType();
        if (!(pType == Token.BLOCK || pType == Token.SCRIPT || pType == Token.NAME || pType == Token.ASSIGN || // object literal keys
            pType == Token.STRING || pType == Token.NUMBER)) {
            return false;
        }
        Node functionParent = n.getParent();
        if (functionParent != null && functionParent.getType() == Token.ASSIGN) {
            Node lhs = functionParent.getFirstChild();
            Node rhs = lhs.getNext();
            if (n == lhs) {
                // Always traverse the left side of the assignment. To handle
                // nested assignments properly (e.g., (a = this).property = c;),
                // assignLhsChild should not be overridden.
                if (assignLhsChild == null) {
                    assignLhsChild = lhs;
                }
            } else if (NodeUtil.isGet(lhs)) {
                // Only traverse the right side if it's not an assignment to a prototype
                // property or subproperty.
                if (lhs.getType() == Token.GETPROP && lhs.getLastChild().getString().equals("prototype")) {
                    return false;
                }
                Node llhs = lhs.getFirstChild();
                if (llhs.getType() == Token.GETPROP && llhs.getLastChild().getString().equals("prototype")) {
                    return false;
                }
            }
        }
    }
    return true;
}