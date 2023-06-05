void tryMinimizeExits(Node n, int exitType, String labelName) {
    // Just an 'exit'.
    if (matchingExitNode(n, exitType, labelName)) {
        NodeUtil.removeChild(n.getParent(), n);
        compiler.reportCodeChange();
        return;
    }
    // Just an 'if'.
    if (n.isIf()) {
        Node ifBlock = n.getFirstChild().getNext();
        tryMinimizeExits(ifBlock, exitType, labelName);
        Node elseBlock = ifBlock.getNext();
        if (elseBlock != null) {
            tryMinimizeExits(elseBlock, exitType, labelName);
        }
        return;
    }
    // Just a 'try/catch/finally'.
    if (n.isTry()) {
        Node tryBlock = n.getFirstChild();
        tryMinimizeExits(tryBlock, exitType, labelName);
        Node allCatchNodes = NodeUtil.getCatchBlock(n);
        if (NodeUtil.hasCatchHandler(allCatchNodes)) {
            Preconditions.checkState(allCatchNodes.hasOneChild());
            Node catchNode = allCatchNodes.getFirstChild();
            Node catchCodeBlock = catchNode.getLastChild();
            tryMinimizeExits(catchCodeBlock, exitType, labelName);
        }
        if (NodeUtil.hasFinally(n)) {
            Node finallyBlock = n.getLastChild();
            tryMinimizeExits(finallyBlock, exitType, labelName);
        }
    }
    // Just a 'label'.
    if (n.isLabel()) {
        Node labelBlock = n.getLastChild();
        tryMinimizeExits(labelBlock, exitType, labelName);
    }
    // TODO(johnlenz): The last case of SWITCH statement?
    // The rest assumes a block with at least one child, bail on anything else.
    if (!n.isBlock() || n.getLastChild() == null) {
        return;
    }
    // Multiple if-exits can be converted in a single pass.
    // Convert "if (blah) break;  if (blah2) break; other_stmt;" to
    // become "if (!blah && !blah2) { other_stmt; }".
    LinkedList<Node> nonExitStatements = new LinkedList<>();
    boolean hasIfExitBlock = tryMinimizeIfBlockExits(n, nonExitStatements, exitType, labelName);
    if (hasIfExitBlock) {
        for (Node nonExit : nonExitStatements) {
            NodeUtil.removeChild(n, nonExit);
            NodeUtil.insertBefore(n.getLastChild(), nonExit);
        }
    }
    // Try to minimize the exits of the last child,
    // if it is removed look at what has become the last child.
    for (Node c = n.getLastChild(); c != null; c = n.getLastChild()) {
        tryMinimizeExits(c, exitType, labelName);
        // If the node is still the last child, we are done.
        if (c == n.getLastChild()) {
            break;
        }
    }
}

/**
 * Tries to minimize the exits of an if block.
 * Returns true if the block had an exit, false otherwise.
 */
private boolean tryMinimizeIfBlockExits(Node n, LinkedList<Node> nonExitStatements, int exitType, String labelName) {
    Preconditions.checkState(n.isBlock(), n);
    if (n.hasChildren() && !n.getLastChild().isControlStructure()) {
        nonExitStatements.add(n.getLastChild());
        return false;
    }
    boolean hasIfExitBlock = false;
    for (Node c : n.children()) {
        if (c.isIf()) {
            Node ifTree = c;
            Node trueBlock = ifTree.getFirstChild().getNext();
            Node falseBlock = trueBlock.getNext();
            boolean trueBlockHasExit = tryMinimizeIfBlockExits(trueBlock, nonExitStatements, exitType, labelName);
            boolean falseBlockHasExit = falseBlock != null && tryMinimizeIfBlockExits(falseBlock, nonExitStatements, exitType, labelName);
            if (trueBlockHasExit || falseBlockHasExit) {
                hasIfExitBlock = true;
                if (falseBlock != null) {
                    Node ifNode = IR.ifNode(ifTree.detachChildren());
                    ifNode.useSourceInfoFromForTree(ifTree);
                    n.replaceChild(c, ifNode);
                    ifNode.getFirstChild().addChildToFront(falseBlock.detach());
                    if (trueBlock.getLastChild().isControlStructure()) {
                        nonExitStatements.add(trueBlock.getLastChild());
                    }
                    if (falseBlock.getLastChild().isControlStructure()) {
                        nonExitStatements.add(falseBlock.getLastChild());
                    }
                    break;
                }
                else {
                    // False block absent.
                    Node ifNode = IR.ifNode(ifTree.detachChildren());
                    ifNode.useSourceInfoFromForTree(ifTree);
                    n.replaceChild(c, ifNode);
                    if (trueBlock.getLastChild().isControlStructure()) {
                        nonExitStatements.add(trueBlock.getLastChild());
                    }
                    break;
                }
            }
        }
        else if (matchingExitNode(c, exitType, labelName)) {
            nonExitStatements.clear();
            nonExitStatements.add(c);
            hasIfExitBlock = true;
            break;
        }
        else if (!c.isControlStructure()) {
            nonExitStatements.add(c);
        }
    }
    return hasIfExitBlock;
}