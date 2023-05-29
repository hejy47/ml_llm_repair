## Fixed Function 1
private Node tryFoldArrayAccess(Node n, Node left, Node right) {
    Node parent = n.getParent();
    // If GETPROP/GETELEM is used as assignment target the array literal is
    // acting as a temporary we can't fold it here:
    //    "[][0] += 1"
    if (NodeUtil.isAssignmentTarget(n)) {
        return n;
    }
    if (!right.isNumber()) {
        // Sometimes people like to use complex expressions to index into
        // arrays, or strings to index into array methods.
        return n;
    }
    double index = right.getDouble();
    int intIndex = (int) index;
    if (intIndex != index || Double.isInfinite(index) || Double.isNaN(index)) {
        error(INVALID_GETELEM_INDEX_ERROR, right);
        return n;
    }
    if (intIndex < 0) {
        error(INDEX_OUT_OF_BOUNDS_ERROR, right);
        return n;
    }
    Node current = left.getFirstChild();
    Node elem = null;
    for (int i = 0; current != null && i < intIndex; i++) {
        elem = current;
        current = current.getNext();
    }
    if (elem == null) {
        error(INDEX_OUT_OF_BOUNDS_ERROR, right);
        return n;
    }
    if (elem.isEmpty()) {
        elem = NodeUtil.newUndefinedNode(n);
    } else {
        left.removeChild(elem);
    }
    // Replace the entire GETELEM with the value
    parent.replaceChild(n, elem.detachFromParent());
    reportCodeChange();
    return elem;
}