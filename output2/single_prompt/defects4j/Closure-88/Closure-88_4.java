## Fixed Function 1
private VariableLiveness isVariableReadBeforeKill(Node n, String variable) {
    if (NodeUtil.isName(n) && variable.equals(n.getString())) {
        if (NodeUtil.isLhs(n, n.getParent())) {
            // The expression to which the assignment is made is evaluated before
            // the RHS is evaluated (normal left to right evaluation) but the KILL
            // occurs after the RHS is evaluated.
            return VariableLiveness.KILL;
        } else {
            // Check if variable is being read before being reassigned
            Node parent = n.getParent();
            boolean assigned = false;
            while (parent != null) {
                if (NodeUtil.isAssignmentOp(parent) && parent.getFirstChild() == n) {
                    assigned = true;
                    break;
                }
                parent = parent.getParent();
            }
            if (!assigned) {
                return VariableLiveness.READ;
            }
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