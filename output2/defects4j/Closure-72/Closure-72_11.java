## Fixed Function 1
private void makeLocalNamesUnique(Node fnNode, boolean isCallInLoop) {
    Supplier<String> idSupplier = compiler.getUniqueNameIdSupplier();
    // Make variable names unique to this instance.
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique(new InlineRenamer(idSupplier, "inline_", isCallInLoop));
    makeDeclaredNamesUnique.process(null, fnNode);
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
            nameNode.setString(newName);
            compiler.reportChangeToEnclosingScope(node);
        }
    } else {
        // ... and it is not referenced, just remove it.
        Node newChild = node.getLastChild();
        NodeUtil.removeChild(parent, node);
        if (newChild.getType() == Token.BLOCK) {
            NodeUtil.tryMergeBlock(newChild);
        }
        compiler.reportChangeToEnclosingScope(parent);
    }
    // Remove the label from the current stack of labels.
    namespaceStack.peek().renameMap.remove(name);
}