## Fixed Function 1
private boolean isFoldableExpressBlock(Node n) {
    if (n.getType() == Token.BLOCK) {
        if (n.getChildCount() == 1) { //Changed to getChildCount() == 1
            Node maybeExpr = n.getFirstChild();
            if (maybeExpr.getType() == Token.EXPR_RESULT) { //Added check for Token.EXPR_RESULT
                Node maybeTarget = maybeExpr.getFirstChild();
                if (maybeTarget.getType() == Token.GETPROP || maybeTarget.getType() == Token.NAME) {
                    return true;
                }
            }
            return NodeUtil.isExpressionNode(maybeExpr);
        }
    }
    return false;
}