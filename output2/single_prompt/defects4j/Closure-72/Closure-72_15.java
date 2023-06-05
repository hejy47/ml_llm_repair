## Fixed Function 1
private void makeLocalNamesUnique(Node fnNode, boolean isCallInLoop) {
    Supplier<String> idSupplier = compiler.getUniqueNameIdSupplier();
    // Make variable names unique to this instance.
    MakeDeclaredNamesUnique makeNamesUnique = new MakeDeclaredNamesUnique(new InlineRenamer(idSupplier, "inline_", isCallInLoop));
    makeNamesUnique.process(fnNode);
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
            Node newNameNode = IR.string(newName);
            node.replaceChild(nameNode, newNameNode);
            compiler.reportCodeChange();
        }
    } else {
        // ... and it is not referenced, just remove it.
        Node newChild = node.getLastChild();
        node.removeChild(nameNode);
        parent.replaceChild(node, newChild);
        if (newChild.getType() == Token.BLOCK) {
            NodeUtil.tryMergeBlock(newChild);
        }
        compiler.reportCodeChange();
    }
    // Remove the label from the current stack of labels.
    namespaceStack.peek().renameMap.remove(name);
}