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
            // into other expressions.
            // We only have to worry about methods with an explicit 'this'
            // param, or this doesn't happen.
            
            // Fix: changed 'implicit' to 'explicit'
            return NodeUtil.isExpressionNode(maybeExpr) && !NodeUtil.isFunctionExpression(maybeExpr);
        }
    }
    return false;
}