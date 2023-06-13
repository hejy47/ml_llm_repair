private void annotateCalls(Node n) {
    Preconditions.checkState(n.isCall());
    // Keep track of the "this" context of a call.  A call without an
    // explicit "this" is a free call.
    Node first = n.getFirstChild();
    // ignore cast nodes.
    if (!NodeUtil.isGet(first)) {
        n.putBooleanProp(Node.FREE_CALL, true);
    }
    // Keep track of the context in which eval is called. It is important
    // to distinguish between "(0, eval)()" and "eval()".
    Node nameNode = first;
    if (NodeUtil.isFunctionObjectWithName(first)) {
        nameNode = first.getFirstChild();
    }
    if (nameNode.isName() && "eval".equals(nameNode.getString())) {
        nameNode.putBooleanProp(Node.DIRECT_EVAL, true);
    }
}