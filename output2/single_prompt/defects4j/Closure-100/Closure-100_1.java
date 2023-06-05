## Fixed Function 1
public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
    if (n.getType() == Token.FUNCTION) {
        // Don't traverse functions that are constructors or have the @this
        // annotation.
        JSDocInfo jsDoc = getFunctionJsDocInfo(n);
        if (jsDoc != null && (jsDoc.isConstructor() || jsDoc.hasThisType())) {
            return false;
        }
        // Don't traverse functions unless they would normally
        // be able to have a @this annotation associated with them. e.g.,
        // var a = function() { }; // or
        // function a() {} // or
        // a.x = function() {};
        if (n.getFirstChild().isName() || n.getFirstChild().isFunction()) {
            return true;
        }
        if (n.getFirstChild().isGetProp()) {
            Node target = n.getFirstChild().getFirstChild();
            // Check if the target is an object-literal.
            if (target != null && target.isObjectLit()) {
                return true;
            }
            // Check if the target is a function call that returns
            // an object-literal.
            if (target != null && target.isCall()) {
                Node functionNode = target.getFirstChild();
                if (functionNode != null && functionNode.isName()) {
                    String functionName = functionNode.getString();
                    if (isCreateObject(functionName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    if (parent != null && parent.getType() == Token.ASSIGN) {
        Node lhs = parent.getFirstChild();
        Node rhs = lhs.getNext();
        if (n == lhs) {
            // Always traverse the left side of the assignment. To handle
            // nested assignments properly (e.g., (a = this).property = c;),
            // assignLhsChild should not be overridden.
            if (assignLhsChild == null) {
                assignLhsChild = lhs;
            }
        } else {
            // Only traverse the right side if it's not an assignment to a prototype
            // property or subproperty.
            if (lhs.getType() == Token.GETPROP) {
                if (lhs.getLastChild().getString().equals("prototype")) {
                    return false;
                }
                String leftName = lhs.getQualifiedName();
                if (leftName != null && leftName.contains(".prototype.")) {
                    return false;
                }
            }
        }
    }
    return true;
}

## Fixed Function 2
private boolean shouldReportThis(Node n, Node parent) {
    if (n.isThis()) {
        if (assignLhsChild != null && !NodeUtil.isLValue(n)) {
            return true;
        }
        if (NodeUtil.isGetProp(n.getParent())) {
            Node target = n.getParent().getFirstChild();
            if (target.getType() == Token.THIS) {
                // report this.someProp
                return true;
            }
            if (target.isName() && isCreateObject(target.getString())) {
                // report goog.createInstance().someProp
                return true;
            }
        }
    }
    return false;
}