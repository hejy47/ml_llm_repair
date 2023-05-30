## Fixed Function 1
private boolean isFoldableExpressBlock(Node n) {
    if (n.getType() == Token.BLOCK) {
        if (n.hasOneChild()) {
            Node maybeExpr = n.getFirstChild();
            // IE has a bug where event handlers behave differently when
            // their return value is used vs. when their return value is in
            // an EXPR_RESULT. It's pretty freaking weird. See:
            // http://code.google.com/p/closure-compiler/issues/detail?id=291
            // We try to detect this case, and not fold EXPR_RESULTs 

            if (NodeUtil.isExpressionNode(maybeExpr)) {
                Node parent = maybeExpr.getParent();
                if (parent == null || parent.isExprResult()) {
                    return true;
                }
            }
        }
    }
    return false;
}