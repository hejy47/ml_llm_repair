private void annotateCalls(Node n) {
    Preconditions.checkState(n.isCall());
    // Keep track of of the "this" context of a call.  A call without an
    // explicit "this" is a free call.
    Node first = n.getFirstChild();
    // ignore cast nodes.
    if (!NodeUtil.isGet(first)) {
        n.putBooleanProp(Node.FREE_CALL, true);
    }
    // Keep track of the context in which eval is called. It is important
    // to distinguish between "(0, eval)()" and "eval()".
    if (first.isName() && "eval".equals(first.getString())) {
        Node parent = n.getParent();
        // check if eval is being called directly or indirectly
        boolean isDirectEval = (parent.isExprResult() || parent.isSwitchCase() || parent.isReturn());
        first.putBooleanProp(Node.DIRECT_EVAL, isDirectEval);
    }
}