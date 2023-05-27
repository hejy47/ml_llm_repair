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
    if (n.isCall() && NodeUtil.functionCallHasSideEffects(n)) {
        return true;
    }
    if (n.isNew() && NodeUtil.constructorCallHasSideEffects(n)) {
        return true;
    }
    for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
        if (!ControlFlowGraph.isEnteringNewCfgNode(c) && apply(c)) {
            return true;
        }
        // If the child node has side effects, then the parent node has side effects too
        if (NodeUtil.isStatementBlock(c) && NodeUtil.mayHaveSideEffects(c, compiler)) {
            return true;
        }
    }
    return false;
}