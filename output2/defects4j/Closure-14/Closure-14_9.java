private static Node computeFollowNode(Node fromNode, Node node, ControlFlowAnalysis cfa) {
    /*
     * This is the case where:
     *
     * 1. Parent is null implies that we are transferring control to the end of
     * the script.
     *
     * 2. Parent is a function implies that we are transferring control back to
     * the caller of the function.
     *
     * 3. If the node is a return statement, we should also transfer control
     * back to the caller of the function.
     *
     * 4. If the node is root then we have reached the end of what we have been
     * asked to traverse.
     *
     * In all cases we should transfer control to a "symbolic return" node.
     * This will make life easier for DFAs.
     */
    Node parent = node.getParent();
    if (parent == null || parent.isFunction() || (cfa != null && node == cfa.root)) {
        return null;
    }
    // If we are just before a IF/WHILE/DO/FOR:
    switch(parent.getType()) {
        // The follow() of any of the path from IF would be what follows IF.
        case Token.IF:
            return computeFollowNode(fromNode, parent, cfa);
        case Token.CASE:
        case Token.DEFAULT_CASE:
            // After the body of a CASE, the control goes to the body of the next
            // case, without having to go to the case condition.
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
        case Token.FOR:
            if (NodeUtil.isForIn(parent)) {
                return parent;
            } else {
                return parent.getFirstChild().getNext().getNext();
            }
        case Token.WHILE:
        case Token.DO:
            return parent;
        case Token.TRY:
            // If we are coming out of the TRY block...
            if (parent.getFirstChild() == node) {
                if (NodeUtil.hasFinally(parent)) {
                    // and have FINALLY block.
                    return computeFallThrough(parent.getLastChild());
                } else {
                    // and have no FINALLY.
                    return computeFollowNode(fromNode, parent, cfa);
                }
                // CATCH block.
            } else if (NodeUtil.getCatchBlock(parent) == node) {
                if (NodeUtil.hasFinally(parent)) {
                    // and have FINALLY block.
                    return computeFallThrough(node.getNext());
                } else {
                    return computeFollowNode(fromNode, parent, cfa);
                }
                // If we are coming out of the FINALLY block...
            } else if (parent.getLastChild() == node) {
                if (cfa != null) {
                    for (Node finallyNode : cfa.finallyMap.get(parent)) {
                        cfa.createEdge(fromNode, Branch.UNCOND, finallyNode);
                    }
                }
                return computeFollowNode(fromNode, parent, cfa);
            }
            break;
    }
    // Now that we are done with the special cases follow should be its
    // immediate sibling, unless its sibling is a function
    Node nextSibling = node.getNext();
    // Skip function declarations because control doesn't get pass into it.
    while (nextSibling != null && nextSibling.isFunction()) {
        nextSibling = nextSibling.getNext();
    }
    if (nextSibling != null) {
        return computeFallThrough(nextSibling);
    } else {
        // If there are no more siblings, control is transferred up the AST.
        return computeFollowNode(fromNode, parent, cfa);
    }
}