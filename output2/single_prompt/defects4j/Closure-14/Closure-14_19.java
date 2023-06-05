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
                    return computeBoundaries(parent.getNext().getFirstChild().getNext());
                } else if (parent.getNext().isDefaultCase()) {
                    return computeBoundaries(parent.getNext().getFirstChild());
                } else {
                    Preconditions.checkState(false, "Not reachable");
                }
            } else {
                return computeFollowNode(fromNode, parent, cfa);
            }
            break;
        case Token.FOR:
            if (NodeUtil.isForIn(parent)) {
                return parent;
            } else if (NodeUtil.isFor(parent)) {
                if (node == NodeUtil.getConditionExpression(parent)) {
                    return computeBoundaries(NodeUtil.getConditionExpression(parent).getFirstChild());
                } else if (NodeUtil.getIncrementExpression(parent) != null && node == NodeUtil.getIncrementExpression(parent)) {
                    return computeBoundaries(NodeUtil.getIncrementExpression(parent).getFirstChild());
                } else {
                    return computeBoundaries(NodeUtil.getLoopBody(parent));
                }
            } else {
                return parent.getFirstChild().getNext().getNext();
            }
        case Token.WHILE:
        case Token.DO:
            return parent;
        case Token.TRY:
            if (parent.getFirstChild() == node) {
                if (NodeUtil.hasFinally(parent)) {
                    return computeBoundaries(NodeUtil.getFinallyBlock(parent));
                } else {
                    return computeFollowNode(fromNode, parent, cfa);
                }
            } else if (NodeUtil.getCatchBlock(parent) == node) {
                if (NodeUtil.hasFinally(parent)) {
                    return computeBoundaries(node.getNext());
                } else {
                    return computeFollowNode(fromNode, parent, cfa);
                }
            } else if (parent.getLastChild() == node) {
                if (cfa != null) {
                    for (Node finallyNode : cfa.finallyMap.get(parent)) {
                        cfa.createEdge(fromNode, Branch.UNCOND, finallyNode);
                    }
                }
                return computeBoundaries(parent.getNext());
            }
    }
    Node nextSibling = node.getNext();
    while (nextSibling != null && nextSibling.isFunction()) {
        nextSibling = nextSibling.getNext();
    }
    if (nextSibling != null) {
        return computeBoundaries(nextSibling);
    } else {
        return computeFollowNode(fromNode, parent, cfa);
    }
}

// Added function to compute start and end boundaries of a node.
private static Node computeBoundaries(Node node) {
    if (node.getFirstChild() != null) {
        return node.getFirstChild();
    } else {
        return node;
    }
}