private static Node computeFollowNode(Node fromNode, Node node, ControlFlowAnalysis cfa) {
    Node parent = node.getParent();
    if (parent == null || parent.isFunction() || (cfa != null && node == cfa.root)) {
        return null;
    }
    switch(parent.getType()) {
        case Token.IF:
            return parent.getNext() != null ? computeFallThrough(parent.getNext()) : computeFollowNode(fromNode, parent, cfa);
        case Token.CASE:
        case Token.DEFAULT_CASE:
            if (parent.getNext() != null) {
                if (parent.getNext().isCase()) {
                    return parent.getNext().getFirstChild().getNext();
                } else if (parent.getNext().isDefaultCase()) {
                    return parent.getNext().getFirstChild();
                } else {
                    Preconditions.checkState(false, "Not reachable");
                }
            } else {
                return computeFollowNode(fromNode, parent, cfa);
            }
            break;
        case Token.FOR:
            if (NodeUtil.isForIn(parent)) {
                return computeFallThrough(parent.getLastChild());
            } else {
                Node nextStatement = NodeUtil.getNextStatement(parent);
                return nextStatement != null ? computeFallThrough(nextStatement) : computeFollowNode(fromNode, parent, cfa);
            }
        case Token.WHILE:
        case Token.DO:
            return parent;
        case Token.TRY:
            Node catchBlock = NodeUtil.getCatchBlock(parent);
            if (parent.getFirstChild() == node) {
                if (NodeUtil.hasFinallyBlock(parent)) {
                    Node finallyBlock = parent.getLastChild();
                    return finallyBlock.getFirstChild();
                }
                return computeFollowNode(fromNode, parent, cfa);
            } else if (catchBlock == node) {
                if (NodeUtil.hasFinallyBlock(parent)) {
                    Node finallyBlock = parent.getLastChild();
                    return finallyBlock.getFirstChild();
                }
                return computeFollowNode(fromNode, parent, cfa);
            } else if (parent.getLastChild() == node) {
                if (cfa != null) {
                    List<Node> finallyList = cfa.getFinallyBlocksForTry(parent);
                    if (finallyList != null) {
                        for (Node finallyNode : finallyList) {
                            cfa.createEdge(fromNode, Branch.UNCOND, finallyNode);
                        }
                        Node nextStatement = NodeUtil.getNextStatement(parent);
                        return nextStatement != null ? computeFallThrough(nextStatement) : computeFollowNode(fromNode, parent, cfa);
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