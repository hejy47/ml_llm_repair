## Fixed Function 1
private static boolean isReduceableFunctionExpression(Node n) {
    return NodeUtil.isFunctionExpression(n) && n.getParent().isExprResult();
}