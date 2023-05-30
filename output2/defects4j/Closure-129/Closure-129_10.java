private void annotateCalls(Node n) {
    Preconditions.checkState(n.isCall());
    // Keep track of of the "this" context of a call.  A call without an
    // explicit "this" is a free call.
    Node first = n.getFirstChild();
    // ignore cast nodes.
    if (!NodeUtil.isGet(first)) {
        n.putBooleanProp(Node.FREE_CALL, true);
    } else if (first.isGetProp() && first.getLastChild().isString() && first.getLastChild().getString().equals("eval")) {
        first.putBooleanProp(Node.DIRECT_EVAL, true);
    }
}