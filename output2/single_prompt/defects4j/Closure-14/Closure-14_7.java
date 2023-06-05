private static Node computeFollowNode(Node fromNode, Node node, ControlFlowAnalysis cfa) {
    Node parent = node.getParent();
    if (parent == null || parent.isFunction() || (cfa != null && node == cfa.root)) {
        return null;
    }
    switch(parent.getType()) {
        case Token.IF:
            return computeFollowNode(fromNode, parent, cfa);
        case Token.CASE:
        case Token.DEFAULT_CASE:
            if (parent.getNext() != null) {
                if (parent.getNext().isCase()) {
                    return parent.getNext().getFirstChild().getNext();
                } else if (parent.getNext().isDefaultCase()) {
                    return parent.getNext().getFirstChild();
                } else {
                    throw new IllegalStateException("Not reachable");
                }
            } else {
                return computeFollowNode(fromNode, parent, cfa);
            }
            break;
        case Token.FOR:
            if (NodeUtil.isForIn(parent)) {
                return parent;
            } else {
                Node update = NodeUtil.getContinueTarget(parent);
                if (update != null) {
                    return update;
                } else {
                    // If there is no update node in the loop, skip the condition and go
                    // straight to the body.
                    return parent.getFirstChild().getNext().getNext();
                }
            }
        case Token.WHILE:
            return parent;
        case Token.DO:
            return parent.getFirstChild();
        case Token.TRY:
            if (parent.getFirstChild() == node) {
                // Coming out of try block
                if (NodeUtil.hasFinally(parent)) {
                    // and have finally block
                    return computeFallThrough(parent.getLastChild());
                } else {
                    // and have no finally block
                    return computeFollowNode(fromNode, parent, cfa);
                }
            } else if (NodeUtil.getCatchBlock(parent) == node) {
                // Coming out of catch block
                if (NodeUtil.hasFinally(parent)) {
                    // and have finally block
                    return computeFallThrough(NodeUtil.getFinallyBlock(parent));
                } else {
                    return computeFollowNode(fromNode, parent, cfa);
                }
            } else {
                // Coming out of finally block
                if (cfa != null) {
                    for (Node finallyNode : cfa.finallyMap.get(parent)) {
                        cfa.createEdge(fromNode, Branch.UNCOND, finallyNode);
                    }
                }
                return computeFollowNode(fromNode, parent, cfa);
            }
    }
    Node nextSibling = node.getNext();
    while (nextSibling != null && nextSibling.isFunction()) {
        nextSibling = nextSibling.getNext();
    }
    if (nextSibling != null) {
        return computeFallThrough(nextSibling);
    } else {
        return computeFollowNode(fromNode, parent, cfa);
    }
}