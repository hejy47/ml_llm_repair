private VariableLiveness isVariableReadBeforeKill(Node n, String variable) {
    if (NodeUtil.isName(n) && variable.equals(n.getString())) {
        if (NodeUtil.isLhs(n, n.getParent())) {
            // The expression to which the assignment is made is evaluated before
            // the RHS is evaluated (normal left to right evaluation) but the KILL
            // occurs after the RHS is evaluated.
            return VariableLiveness.KILL;
        } else {
            return VariableLiveness.READ;
        }
    }
    // Expressions are evaluated left-right, depth first.
    for (Node child = n.getFirstChild(); child != null; child = child.getNext()) {
        if (!ControlFlowGraph.isEnteringNewCfgNode(child)) {
            // Not a FUNCTION
            VariableLiveness state = isVariableReadBeforeKill(child, variable);
            if (state != VariableLiveness.MAYBE_LIVE) {
                return state;
            }
        } else {
            // Ensure to keep track of variable use inside a function.
            // This is necessary to avoid incorrectly marking variables as live
            // after they have been killed inside a function.
            if (NodeUtil.isFunctionBlock(child)) {
                VariableLiveness state = isVariableReadBeforeKill(child.getLastChild(), variable);
                if (state != VariableLiveness.MAYBE_LIVE) {
                    return state;
                }
            }
        }
    }
    return VariableLiveness.MAYBE_LIVE;
}