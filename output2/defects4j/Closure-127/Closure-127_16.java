@SuppressWarnings("fallthrough")
private void tryRemoveUnconditionalBranching(Node n) {
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
            List<DiGraphEdge<Node, Branch>> outEdges = gNode.getOutEdges();
            if (outEdges.size() == 1 &&
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