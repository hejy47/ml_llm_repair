## Fixed Function 1
private static boolean isReduceableFunctionExpression(Node n) {
    return n.isFunction() && !n.getFirstChild().isFunctionName() && !n.getBooleanProp(Node.IS_ARROW_FN);
}