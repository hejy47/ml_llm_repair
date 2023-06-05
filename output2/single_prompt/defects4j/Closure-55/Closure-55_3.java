## Fixed Function 1
private static boolean isReduceableFunctionExpression(Node n) {
    return NodeUtil.isFunction(n) || NodeUtil.isArrowFunction(n);
}