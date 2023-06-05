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
        Node catchBlock = NodeUtil.getCatchBlock(n);
        if (catchBlock != null) {
            Preconditions.checkState(catchBlock.hasOneChild());
            Node catchCodeBlock = catchBlock.getFirstChild();
            tryMinimizeExits(catchCodeBlock, exitType, labelName);
        }
        if (NodeUtil.hasFinally(n)) {
            Node finallyBlock = NodeUtil.getFinallyBlock(n);
            tryMinimizeExits(finallyBlock, exitType, labelName);
        }
    }
    // Just a 'label'.
    if (n.isLabel()) {
        Node labelBlock = n.getLastChild();
        tryMinimizeExits(labelBlock, exitType, labelName);
        return;
    }
    // The rest assumes a block with at least one child.
    if (!n.isBlock() || n.getLastChild() == null) {
        return;
    }
    // Multiple if-exits can be converted in a single pass.
    // Convert "if (blah) break;  if (blah2) break; other_stmt;" to
    // become "if (blah); else { if (blah2); else { other_stmt; } }"
    // which will get converted to "if (!blah && !blah2) { other_stmt; }".
    for (Node c : n.children()) {
        // An 'if' block to process below.
        if (c.isIf()) {
            Node ifTree = c;
            Node trueBlock = ifTree.getFirstChild().getNext();
            Node falseBlock = trueBlock.getNext();

            // Fix the true block.
            tryMinimizeIfBlockExits(trueBlock, falseBlock, ifTree, exitType, labelName);

            // Fix the else block only if it exists.
            if (falseBlock != null) {
                tryMinimizeIfBlockExits(falseBlock, trueBlock, ifTree, exitType, labelName);
            }
        }

        // Recurse on this child of n.
        tryMinimizeExits(c, exitType, labelName);

        // Once the child is processed it may go away and be replaced
        // with the next sibling, so only continue if c is still a child of n.
        if (c.getParent() != n) {
            break;
        }
    }
}