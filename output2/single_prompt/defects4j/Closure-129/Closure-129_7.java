private void annotateCalls(Node n) {
    Preconditions.checkState(n.isCall());
    // Keep track of the "this" context of a call.  A call without an
    // explicit "this" is a free call.
    Node first = n.getFirstChild();
    // ignore cast nodes.
    if (!NodeUtil.isGet(first) && !NodeUtil.isName(first)) { 
        // Added a condition to check if the node is a name node.
        n.putBooleanProp(Node.FREE_CALL, true);
    }
    // Keep track of the context in which eval is called. It is important
    // to distinguish between "(0, eval)()" and "eval()".
    if (first.isName() && "eval".equals(first.getString())) {
        first.putBooleanProp(Node.DIRECT_EVAL, true);
    }
}