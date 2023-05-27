## Fixed Function 1
@Override
public boolean apply(Node n) {
    // When the node is null it means, we reached the implicit return
    // where the function returns (possibly without a return statement)
    if (n == null) {
        return false;
    }
    // We only care about calls to functions that pass one of the dependent variables
    if (n.isCall() && !NodeUtil.functionCallHasNoSideEffects(n)) {
        for (int i = 1; i < n.getChildCount(); i++) {
            if (dependencies.contains(n.getChildAt(i))) {
                return true;
            }
        }
    }
    // We only care about constructors that pass one of the dependent variables
    if (n.isNew() && !NodeUtil.constructorCallHasNoSideEffects(n)) {
        for (int i = 0; i < n.getChildCount(); i++) {
            if (dependencies.contains(n.getChildAt(i))) {
                return true;
            }
        }
    }
    // Recurse on all non-control-flow children
    for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
        if (!ControlFlowGraph.isEnteringNewCfgNode(c) && apply(c)) {
            return true;
        }
    }
    return false;
}