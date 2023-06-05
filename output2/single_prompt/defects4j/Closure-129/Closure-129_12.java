private void annotateCalls(Node n) {
    Preconditions.checkState(n.isCall());
    // Keep track of the "this" context of a call.  A call without an
    // explicit "this" is a free call.
    Node first = n.getFirstChild();
    // ignore cast nodes.
    if (!NodeUtil.isGet(first)) {
        n.putBooleanProp(Node.FREE_CALL, true);
    } else {
        annotateGet(first);
    }
    // Keep track of the context in which eval is called. It is important
    // to distinguish between "(0, eval)()" and "eval()".
    if (first.isName() && "eval".equals(first.getString())) {
        first.putBooleanProp(Node.DIRECT_EVAL, true);
    }
}

private void annotateGet(Node get) {
    for (Node child = get.getFirstChild(); child != null; child = child.getNext()) {
        if (child.isGetProp()) {
            annotateGet(child);
        } else if (child.isName()) {
            String className = getQualifiedNameOfNode(child);
            if (className != null && "window".equals(className)) {
                get.putBooleanProp(Node.FREE_CALL, true);
            }
        }
    }
}

private String getQualifiedNameOfNode(Node node) {
    if (node == null) {
        return null;
    }
    if (node.isName()) {
        return node.getString();
    } else if (node.isGetProp()) {
        return getQualifiedNameOfNode(node.getFirstChild()) + "." + node.getLastChild().getString();
    } else {
        return null;
    }
}