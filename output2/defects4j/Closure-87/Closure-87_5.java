## Fixed Function 1
private boolean isFoldableExpressBlock(Node n) {
    if (n.getType() == Token.BLOCK) {
        if (n.getChildCount() == 1) { // check if there is only one child
            Node maybeExpr = n.getFirstChild();
            // IE has a bug where event handlers behave differently when
            // their return value is used vs. when their return value is in
            // an EXPR_RESULT. It's pretty freaking weird. See:
            // http://code.google.com/p/closure-compiler/issues/detail?id=291
            // We try to detect this case, and not fold EXPR_RESULTs
            // into other expressions.
            // We only have to worry about methods with an implicit 'this'
            // param, or this doesn't happen.
            return (NodeUtil.isExpressionNode(maybeExpr) && !maybeExpr.isExprResult()); // check if the child is an expression and not an EXPR_RESULT
        }
    }
    return false;
}