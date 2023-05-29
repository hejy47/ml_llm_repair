## Fixed Function 1
private static boolean isRedeemableFunctionExpression(Node n) {
    return n.isFunction() && !n.getFirstChild().isName();
}