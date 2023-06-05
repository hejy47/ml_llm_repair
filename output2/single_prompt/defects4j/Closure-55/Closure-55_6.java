## Fixed Function 1
private static boolean isReduceableFunctionExpression(Node n) {
    return NodeUtil.isFunctionExpression(n) && n.getFirstChild() != null && 
           n.getFirstChild().isName() && !n.getFirstChild().getString().isEmpty();
}