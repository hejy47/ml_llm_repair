## Fixed Function 1
private boolean isVariableStillLiveWithinExpression(Node n, Node exprRoot, String variable) {
    while (n != exprRoot) {
        VariableLiveness state = VariableLiveness.MAYBE_LIVE;
        switch(n.getParent().getType()) {
            case Token.OR:
            case Token.AND:
                // If the current node is the first child of AND/OR, be conservative and
                // only consider the READs of the second operand.
                if (n == n.getParent().getFirstChild()) {
                    n = n.getParent().getLastChild();
                    break;
                }
                // If the current node is the second child of AND/OR, consider all siblings
                // before it as they are guaranteed to be evaluated first.
                for (Node sibling = n.getPrevious(); sibling != null; sibling = sibling.getPrevious()) {
                    if (!ControlFlowGraph.isEnteringNewCfgNode(sibling)) {
                        state = isVariableReadBeforeKill(sibling, variable);
                        // If we see a READ or KILL there is no need to continue.
                        if (state == VariableLiveness.READ) {
                            return true;
                        } else if (state == VariableLiveness.KILL) {
                            return false;
                        }
                    }
                }
                n = n.getParent();
                break;
            case Token.HOOK:
                // If the current node is the condition, check each branch.
                if (n == n.getParent().getFirstChild()) {
                    n = n.getFirstChild().getNext(); // skip the true branch
                    break;
                } else {
                    // If the current node is the false branch, fall through to default case.
                }
            default:
                // For all other node types, consider all siblings before and after the current node
                // as they are guaranteed to be evaluated before and after it respectively.
                for (Node sibling = n.getNext(); sibling != null; sibling = sibling.getNext()) {
                    if (!ControlFlowGraph.isEnteringNewCfgNode(sibling)) {
                        state = isVariableReadBeforeKill(sibling, variable);
                        // If we see a READ or KILL there is no need to continue.
                        if (state == VariableLiveness.READ) {
                            return true;
                        } else if (state == VariableLiveness.KILL) {
                            return false;
                        }
                    }
                }
                for (Node sibling = n.getPrevious(); sibling != null; sibling = sibling.getPrevious()) {
                    if (!ControlFlowGraph.isEnteringNewCfgNode(sibling)) {
                        state = isVariableReadBeforeKill(sibling, variable);
                        // If we see a READ or KILL there is no need to continue.
                        if (state == VariableLiveness.READ) {
                            return true;
                        } else if (state == VariableLiveness.KILL) {
                            return false;
                        }
                    }
                }
                n = n.getParent();
                break;
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
            // the RHS is evaluated (normal left-to-right evaluation) but the KILL
            // occurs after the RHS is evaluated. We follow the RHS and return READ
            // if the variable is read anywhere in it.
            Node rhs = n.getNext();
            return isVariableReadBeforeKill(rhs, variable);
        } else {
            return VariableLiveness.READ;
        }
    }
    switch(n.getType()) {
        // Conditionals
        case Token.OR:
        case Token.AND:
            // Consider the first branch fully, but only consider READs of the second branch
            // because it may be skipped depending on the result of the first.
            Node trueBranch = n.getFirstChild();
            Node falseBranch = trueBranch.getNext();
            VariableLiveness state = isVariableReadBeforeKill(trueBranch, variable);
            if (state == VariableLiveness.READ) {
                return state;
            } else {
                return isVariableReadBeforeKill(falseBranch, variable);
            }
        case Token.HOOK:
            // Consider both branches fully.
            Node condition = n.getFirstChild();
            trueBranch = condition.getNext();
            falseBranch = trueBranch.getNext();
            if (isVariableReadBeforeKill(condition, variable) == VariableLiveness.READ) {
                return VariableLiveness.READ;
            } else if (isVariableReadBeforeKill(trueBranch, variable) == VariableLiveness.READ) {
                return VariableLiveness.READ;
            } else {
                return isVariableReadBeforeKill(falseBranch, variable);
            }
        default:
            // Expressions are evaluated left-to-right, depth-first.
            for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
                if (!ControlFlowGraph.isEnteringNewCfgNode(child)) {
                    // Not a FUNCTION
                    state = isVariableReadBeforeKill(child, variable);
                    if (state != VariableLiveness.MAYBE_LIVE) {
                        return state;
                    }
                }
            }
            return VariableLiveness.MAYBE_LIVE;
    }
}