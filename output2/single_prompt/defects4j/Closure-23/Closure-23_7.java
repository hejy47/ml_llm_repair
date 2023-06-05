private Node tryFoldArrayAccess(Node n, Node left, Node right) {
    Node parent = n.getParent();
    // If GETPROP/GETELEM is used as assignment target the array literal is
    // acting as a temporary we can't fold it here:
    //    "[][0] += 1"
    if (isAssignmentTarget(n)) {
        return n;
    }
    if (!right.isNumber()) {
        // Sometimes people like to use complex expressions to index into
        // arrays, or strings to index into array methods.
        return n;
    }
    double index = right.getDouble();
    int intIndex = (int) index;
    if (intIndex != index || index < 0) { // consider negative integer index as invalid
        error(INVALID_GETELEM_INDEX_ERROR, right);
        return n;
    }
    Node current = left.getFirstChild();
    Node elem = null;
    for (int i = 0; current != null && i < intIndex; i++) {
        elem = current;
        current = current.getNext();
    }
    if (current == null) { // check if the index is out of bounds
        error(INDEX_OUT_OF_BOUNDS_ERROR, right);
        return n;
    }
    if (elem == null) {
        elem = NodeUtil.newUndefinedNode(elem);
    } else {
        left.removeChild(elem);
    }
    // Replace the entire GETELEM with the value
    n.getParent().replaceChild(n, elem);
    reportCodeChange();
    return elem;
}