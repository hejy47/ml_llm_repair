## Fixed Function 1
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
            VariableLiveness state = isVariableReadBeforeKill(child, variable);
            // Check if the variable is killed before being read in a nested child node
            if (state == VariableLiveness.READ && NodeUtil.isLhs(n, child)) {
                return VariableLiveness.KILL;
            }
            if (state != VariableLiveness.MAYBE_LIVE) {
                return state;
            }
        }
    }
    return VariableLiveness.MAYBE_LIVE;
}