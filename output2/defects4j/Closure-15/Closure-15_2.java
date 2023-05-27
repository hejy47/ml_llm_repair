## Fixed Function 1
@Override
public boolean apply(Node n) {
    // When the node is null it means, we reached the implicit return
    // where the function returns (possibly without an return statement)
    if (n == null) {
        return false;
    }
    // TODO(user): We only care about calls to functions that
    // passes one of the dependent variable to a non-side-effect free
    // function.
    if (n.isCall() && !NodeUtil.functionCallHasPureResult(n)) {
        return true;
    }
    if (n.isNew() && !NodeUtil.constructorCallHasPureResult(n)) {
        return true;
    }
    for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
        if (!ControlFlowGraph.isEnteringNewCfgNode(c) && apply(c)) {
            return true;
        }
    }
    return false;
}