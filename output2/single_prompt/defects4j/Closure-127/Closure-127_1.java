@SuppressWarnings("fallthrough")
private void tryRemoveUnconditionalBranching(Node n) {
    // If n is null the target is the end of the function, nothing to do.
    if (n == null) {
        return;
    }
    DiGraphNode<Node, Branch> gNode = cfg.getDirectedGraphNode(n);
    if (gNode == null) {
        return;
    }
    switch(n.getType()) {
        case Token.RETURN:
            if (n.hasChildren()) {
                break;
            }
            // fallthrough
        case Token.BREAK:
        case Token.CONTINUE:
            // We are looking for a control flow changing statement that always
            // branches to the same node. If after removing it control still
            // branches to the same node, it is safe to remove.
            List<DiGraphEdge<Node, Branch>> outEdges = gNode.getOutEdges();
            if (outEdges.size() == 1 && // If there is a next node, this jump is not useless.
            (n.getNext() == null || n.getNext().isFunction())) {
                Preconditions.checkState(outEdges.get(0).getValue() == Branch.UNCOND);
                Node fallThrough = computeFollowing(n);
                Node nextCfgNode = outEdges.get(0).getDestination().getValue();
                if (nextCfgNode == fallThrough) {
                    removeNode(n);
                }
            }
            break;
    }
}