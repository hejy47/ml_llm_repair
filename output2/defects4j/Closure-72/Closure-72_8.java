## Fixed Function 1
private void makeLocalNamesUnique(Node fnNode, boolean isCallInLoop) {
    Supplier<String> idSupplier = compiler.getUniqueNameIdSupplier();
    // Make variable names unique to this instance.
    NodeTraversal.traverse(compiler, fnNode, new MakeDeclaredNamesUnique(new InlineRenamer(idSupplier, "inline_", isCallInLoop)));
    // Make label names unique to this instance.
    NodeTraversal.traverse(compiler, fnNode, new MakeDeclaredNamesUnique(new LabelRenamer(idSupplier)));
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
            compiler.reportCodeChange();
        }
    } else {
        // ... and it is not referenced, just remove it.
        NodeUtil.removeChild(parent, node);
        compiler.reportCodeChange();
    }
    // Remove the label from the current stack of labels.
    namespaceStack.peek().renameMap.remove(name);
}