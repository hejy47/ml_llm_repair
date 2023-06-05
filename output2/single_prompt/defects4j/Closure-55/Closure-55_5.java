## Fixed Function 1
private static boolean isReduceableFunctionExpression(Node n) {
    if (NodeUtil.isFunctionExpression(n)) {
        FunctionNode fn = (FunctionNode) n;
        if (fn.getBody() == null || fn.getBody().getLastChild() == null) {
            return true;
        }
    }
    return false;
}