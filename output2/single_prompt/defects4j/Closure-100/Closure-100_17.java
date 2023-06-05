## Fixed Function 1
public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
    if (n.isFunction()) {
        // Don't traverse functions that are constructors or have the @this
        // annotation.
        JSDocInfo jsDoc = n.getJSDocInfo();
        if (jsDoc != null && (jsDoc.isConstructor() || jsDoc.hasThisType())) {
            return false;
        }
        // Don't traverse functions unless they would normally
        // be able to have a @this annotation associated with them. e.g.,
        // var a = function() { }; // or
        // function a() {} // or
        // a.x = function() {};
        return NodeUtil.mayHaveSideEffects(n);
    }
    if (parent != null && parent.isAssign()) {
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
            boolean lhsIsPrototype = lhs.isGetProp()
                    && lhs.getLastChild().getString().equals("prototype");
            boolean lhsContainsPrototype = lhs
                    .getQualifiedName()
                    .contains(".prototype.");
            if (lhsIsPrototype || lhsContainsPrototype) {
                return false;
            }
        }
    }
    return true;
}

## Fixed Function 2
private boolean shouldReportThis(Node n, Node parent) {
    if (assignLhsChild != null && NodeUtil.isThis(n)) {
        // Always report a THIS on the left side of an assign.
        return true;
    }
    // Also report a THIS with a property access.
    return n.isGetProp() && NodeUtil.isThis(n.getFirstChild());
}