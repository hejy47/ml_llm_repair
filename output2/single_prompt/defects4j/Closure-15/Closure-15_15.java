## Fixed Function 1
@Override
public boolean apply(Node n) {
    // When the node is null it means, we reached the implicit return
    // where the function returns (possibly without a return statement)
    if (n == null) {
        return false;
    }

    // TODO(user): We only care about calls to functions that
    // pass one of the dependent variables to a non-side-effect free
    // function.
    if (n.isCall() && !NodeUtil.functionCallHasSideEffects(n)) {
        Node target = n.getFirstChild();
        if (target == null) {
            return false;
        }
        // Check if the function being called is a pure function
        String functionName = target.getQualifiedName();
        if (functionName != null && purityAnalyzer.isPure(functionName)) {
            return false;
        }
        return true;
    }
    if (n.isNew() && NodeUtil.constructorCallHasSideEffects(n)) {
        return true;
    }
    for (Node c = n.getFirstChild(); c != null; c = c.getNext()) {
        if (!ControlFlowGraph.isEnteringNewCfgNode(c) && apply(c)) {
            return true;
        }
    }
    return false;
}