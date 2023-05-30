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
}

/**
* Keep track of the context in which eval is called. It is important
* to distinguish between "(0, eval)()" and "eval()".
*/
private void annotateGet(Node get) {
    Preconditions.checkState(NodeUtil.isGet(get));
    Node recv = get.getFirstChild();
    if (recv.isName() && "eval".equals(recv.getString())) {
        recv.putBooleanProp(Node.DIRECT_EVAL, true);
    }
}