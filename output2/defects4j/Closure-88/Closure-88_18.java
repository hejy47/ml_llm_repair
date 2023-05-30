private VariableLiveness isVariableReadBeforeKill(Node n, String variable) {
    if (NodeUtil.isName(n) && variable.equals(n.getString())) {
        if (NodeUtil.isLhs(n, n.getParent())) {
            // The expression to which the assignment is made is evaluated before
            // the RHS is evaluated (normal left to right evaluation) but the KILL
            // occurs after the RHS is evaluated.
            return VariableLiveness.KILL;
        } else {
            // Check if the variable is used before being assigned
            Node parent = n.getParent();
            while (parent != null) {
                if (NodeUtil.isAssign(parent)) {
                    if (NodeUtil.isLhs(n, parent)) {
                        // The variable is being assigned
                        return VariableLiveness.WRITE;
                    } else {
                        // The variable is being read
                        return VariableLiveness.READ;
                    }
                }
                parent = parent.getParent();
            }
            // The variable is not being used before being assigned, but it could still be live
            return VariableLiveness.MAYBE_LIVE;
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
        }
    }
    return VariableLiveness.MAYBE_LIVE;
}