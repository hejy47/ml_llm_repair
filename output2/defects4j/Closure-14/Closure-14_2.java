private static Node computeFollowNode(Node fromNode, Node node, ControlFlowAnalysis cfa) {
    Node parent = node.getParent();
    if (parent == null || parent.isFunction() || (cfa != null && node == cfa.root)) {
        return null;
    }
    switch(parent.getType()) {
        case Token.IF:
            // The follow() of any of the path from IF would be what follows IF.
            return computeFollowNode(fromNode, parent, cfa);
        case Token.CASE:
        case Token.DEFAULT_CASE:
            // After the body of a CASE, the control goes to the body of the next
            // case, without having to go to the case condition.
            if (parent.getNext() != null) {
                if (parent.getNext().isCase()) {
                    // Check if case has any children, get next item
                    if (parent.getNext().hasChildren()) {
                        return NodeUtil.getSuccessor(parent.getNext().getLastChild());
                    } else {
                        return computeFollowNode(fromNode, parent, cfa);
                    }
                } else if (parent.getNext().isDefaultCase()) {
                    // If default case has any children, get next item
                    if (parent.getNext().hasChildren()) {
                        return NodeUtil.getSuccessor(parent.getNext().getFirstChild());
                    } else {
                        return computeFollowNode(fromNode, parent, cfa);
                    }
                } 
            }
            return computeFollowNode(fromNode, parent, cfa);
        case Token.FOR:
            if (NodeUtil.isForIn(parent)) {
                // If FOR loop in, get target (second child)
                return parent.getSecondChild();
            } else {
                // If regular FOR loop, get body (third child)
                return parent.getLastChild();
            }
        case Token.WHILE:
        case Token.DO:
            // Get the body of the loop
            return parent.getLastChild();
        case Token.TRY:
            if (parent.getFirstChild() == node) {
                if (NodeUtil.hasFinally(parent)) {
                    // and have FINALLY block.
                    return computeFallThrough(parent.getLastChild());
                } else {
                    // and have no FINALLY.
                    return computeFollowNode(fromNode, parent, cfa);
                }
            } else if (NodeUtil.getCatchBlock(parent) == node) {
                if (NodeUtil.hasFinally(parent)) {
                    // and have FINALLY block.
                    return computeFallThrough(node.getNext());
                } else {
                    return computeFollowNode(fromNode, parent, cfa);
                }
            } else if (parent.getLastChild() == node) {
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