## Fixed Function 1
private boolean isVariableStillLiveWithinExpression(Node n, Node exprRoot, String variable) {
    while (n != exprRoot) {
        VariableLiveness state = VariableLiveness.MAYBE_LIVE;
        switch(n.getParent().getType()) {
            case Token.OR:
            case Token.AND:
            case Token.HOOK:
                boolean considerNext = false;
                // If the currently node is the first child of
                // AND/OR/HOOK, be conservative only consider the READs
                // of the second operand/branch.
                if (n.getNext() == null && n.getPrevious() != null) {
                    considerNext = true;
                    n = n.getPrevious();
                } else if (n.getNext() != null) {
                    n = n.getNext();
                } else {
                    n = n.getParent();
                }
                for (Node sibling = n; sibling != null; sibling = considerNext ? sibling.getPrevious() : sibling.getNext()) {
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
            default:
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
            VariableLiveness state = isVariableReadBeforeKill(rhs, variable);
            if (state == VariableLiveness.READ) {
                return state;
            }
            return VariableLiveness.KILL;
        } else {
            return VariableLiveness.READ;
        }
    }
    switch(n.getType()) {
        // Conditionals
        case Token.OR:
        case Token.AND:
        case Token.HOOK:
            return checkHookBranchReadBeforeKill(n.getFirstChild(), n.getLastChild(), variable);
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