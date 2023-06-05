## Fixed Function 1
private boolean isVariableStillLiveWithinExpression(Node n, Node exprRoot, String variable) {
    while (n != exprRoot) {
        VariableLiveness state = VariableLiveness.MAYBE_LIVE;
        switch(n.getParent().getType()) {
            case Token.OR:
            case Token.AND:
                // If the current node is the second child of an AND/OR,
                // ignore the first operand since it won't affect the
                // liveness of the variable.
                if (n == n.getParent().getLastChild()) {
                    state = isVariableReadBeforeKill(n, variable);
                }
                n = n.getParent();
                break;
            case Token.HOOK:
                // Check each branch of the ternary operator only if
                // the current node is the condition.
                if (n == n.getParent().getFirstChild()) {
                    Node trueExpr = n.getNext();
                    Node falseExpr = trueExpr.getNext();
                    boolean trueExprLive = isVariableStillLiveWithinExpression(trueExpr, trueExpr.getLastChild(), variable);
                    boolean falseExprLive = isVariableStillLiveWithinExpression(falseExpr, falseExpr.getLastChild(), variable);
                    // If the variable is live in both branches, it is live
                    // in the entire ternary expression.
                    if (trueExprLive && falseExprLive) {
                        return true;
                    }
                    // If the variable is only live in one of the branches,
                    // it may be live in the entire expression.
                    state = trueExprLive || falseExprLive ? VariableLiveness.MAYBE_LIVE : VariableLiveness.NOT_LIVE;
                } else {
                    n = n.getParent();
                }
                break;
            default:
                // For all other nodes, check if the variable is read
                // before it is killed.
                for (Node sibling = n.getNext(); sibling != null; sibling = sibling.getNext()) {
                    if (!ControlFlowGraph.isEnteringNewCfgNode(sibling)) {
                        state = isVariableReadBeforeKill(sibling, variable);
                        if (state == VariableLiveness.READ) {
                            return true;
                        } else if (state == VariableLiveness.KILL) {
                            return false;
                        }
                    }
                }
                n = n.getParent();
        }
    }
    return false;
}

## Fixed Function 2
private VariableLiveness isVariableReadBeforeKill(Node n, String variable) {
    if (NodeUtil.isName(n) && variable.equals(n.getString())) {
        if (NodeUtil.isLhs(n, n.getParent())) {
            Preconditions.checkState(n.getParent().getType() == Token.ASSIGN);
            // The expression to which the assignment is made is evaluated before
            // the RHS is evaluated (normal left to right evaluation) but the KILL
            // occurs after the RHS is evaluated.
            Node rhs = n.getNext();
            // Check if the variable is read before it is killed by the assignment.
            VariableLiveness state = VariableLiveness.MAYBE_LIVE;
            for (Node child = rhs.getFirstChild(); child != null; child = child.getNext()) {
                if (!ControlFlowGraph.isEnteringNewCfgNode(child)) {
                    // Not a FUNCTION
                    VariableLiveness childState = isVariableReadBeforeKill(child, variable);
                    if (childState == VariableLiveness.READ) {
                        return childState;
                    } else if (childState == VariableLiveness.KILL) {
                        state = VariableLiveness.KILL;
                    }
                }
            }
            return state;
        } else {
            return VariableLiveness.READ;
        }
    }
    switch(n.getType()) {
        // Conditionals
        case Token.OR:
        case Token.AND:
            // If the variable is not live on the first branch of an
            // AND, it does not need to be checked for the second branch.
            // If the variable is not live on the second branch of an OR,
            // it does not need to be checked for the first branch.
            if ((n.getType() == Token.AND && !isVariableStillLiveWithinExpression(n.getNext(), n.getParent().getLastChild(), variable))
                    || (n.getType() == Token.OR && !isVariableStillLiveWithinExpression(n.getNext(), n.getParent().getLastChild(), variable))) {
                return VariableLiveness.NOT_LIVE;
            }
            // With an AND/OR the first branch always runs, but the second may not.
            VariableLiveness state = isVariableReadBeforeKill(n.getFirstChild(), variable);
            return state == VariableLiveness.READ ? state : isVariableReadBeforeKill(n.getLastChild(), variable);
        case Token.HOOK:
            // When dealing with a ternary operator, combine the liveness
            // of the variable on both branches.
            boolean trueLive = isVariableStillLiveWithinExpression(n.getFirstChild().getNext(), n.getLastChild(), variable);
            boolean falseLive = isVariableStillLiveWithinExpression(n.getLastChild().getNext(), n.getLastChild(), variable);
            if (trueLive && falseLive) {
                return VariableLiveness.MAYBE_LIVE;
            } else if (trueLive) {
                return VariableLiveness.READ;
            } else if (falseLive) {
                return VariableLiveness.KILL;
            } else {
                return VariableLiveness.NOT_LIVE;
            }
        default:
            // Expressions are evaluated left-right, depth first.
            for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
                if (!ControlFlowGraph.isEnteringNewCfgNode(child)) {
                    // Not a FUNCTION
                    VariableLiveness state = isVariableReadBeforeKill(child, variable);
                    if (state != VariableLiveness.MAYBE_LIVE) {
                        return state;
                    }
                }
            }
    }
    return VariableLiveness.MAYBE_LIVE;
}