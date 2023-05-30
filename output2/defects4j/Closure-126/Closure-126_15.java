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
    // become "if (blah); else { if (blah2); else { other_stmt; } }"
    // which will get converted to "if (!blah && !blah2) { other_stmt; }".
    Iterator<Node> iterator = n.children().iterator();
    Node prev = null;
    while (iterator.hasNext()) {
        Node c = iterator.next();
        if (c.isIf() && prev != null && prev.isIf()) {
            // Merge if blocks
            Node ifTree1 = prev;
            Node ifTree2 = c;
            Node trueBlock1 = ifTree1.getFirstChild().getNext();
            Node falseBlock1 = trueBlock1.getNext();
            Node trueBlock2 = ifTree2.getFirstChild().getNext();
            Node falseBlock2 = trueBlock2.getNext();
            Node trueBlock = IR.block();
            Node falseBlock = IR.block();
            // Build nested if block
            ifTree1.replaceChild(falseBlock1, IR.ifNode(falseBlock1.detach(), trueBlock.detach()));
            ifTree1.replaceChild(trueBlock1, IR.ifNode(trueBlock1.detach(), falseBlock));
            // Merge true blocks
            trueBlock.addChildToBack(trueBlock1.detach());
            trueBlock.addChildToBack(trueBlock2.detach());
            // Merge false blocks
            if (falseBlock1.hasChildren() && falseBlock2.hasChildren()) {
                Node tempTrue = IR.block();
                Node tempFalse = IR.block();
                tryMinimizeIfBlockExits(falseBlock1, falseBlock2, ifTree2, exitType, labelName);
                tempTrue.addChildrenToFront(falseBlock1.removeChildren());
                tempFalse.addChildrenToFront(falseBlock2.removeChildren());
                falseBlock.addChildToBack(IR.ifNode(tempTrue.detach(), tempFalse.detach()));
            } else if (falseBlock1.hasChildren()) {
                falseBlock.addChildrenToFront(falseBlock1.removeChildren());
            } else if (falseBlock2.hasChildren()) {
                falseBlock.addChildrenToFront(falseBlock2.removeChildren());
            }
            NodeUtil.removeChild(n, ifTree2);
            compiler.reportCodeChange();
            c = ifTree1;
            iterator = n.children().iterator();
        }
        tryMinimizeExits(c, exitType, labelName);
        prev = c;
    }
}