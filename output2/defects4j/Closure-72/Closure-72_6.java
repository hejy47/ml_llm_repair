## Fixed Function 1
private void makeLocalNamesUnique(Node fnNode, boolean isCallInLoop) {
    Supplier<String> idSupplier = compiler.getUniqueNameIdSupplier();
    // Make variable names unique to this instance.
    MakeDeclaredNamesUnique nameUnique = new MakeDeclaredNamesUnique(new InlineRenamer(idSupplier, "inline_", isCallInLoop));
    nameUnique.process(fnNode);
    // Make label names unique to this instance.
}

## Fixed Function 2
private void visitLabel(Node node, Node parent) {
    Node nameNode = node.getFirstChild();
    Preconditions.checkState(nameNode != null);
    String name = nameNode.getString();
    LabelInfo li = getLabelInfo(name);
    // This is a label...
    if (li.referenced) {
        String newName = getNameForId(li.id);
        if (!name.equals(newName)) {
            // ... and it is used, give it the short name.
            NodeLabel label = NodeUtil.newLabel(compiler, newName);
            parent.replaceChild(node, label);
            label.addChildToFront(node.getLastChild());
            compiler.reportCodeChange();
        }
    } else {
        // ... and it is not referenced, just remove it.
        node.detach();
        if (parent.getType() == Token.LABEL) {
            parent.getParent().replaceChild(parent, node.getLastChild());
        } else {
            parent.replaceChild(node, node.getLastChild());
        }
        if (node.getLastChild().getType() == Token.BLOCK) {
            NodeUtil.tryMergeBlock(node.getLastChild());
        }
        compiler.reportCodeChange();
    }
    // Remove the label from the current stack of labels.
    namespaceStack.peek().renameMap.remove(name);
}