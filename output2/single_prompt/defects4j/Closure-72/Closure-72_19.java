## Fixed Function 1
private void makeLocalNamesUnique(Node fnNode, boolean isCallInLoop) {
    Supplier<String> idSupplier = compiler.createUniqueNameIdSupplier();
    // Make variable names unique to this instance.
    NodeTraversal.traverse(compiler, fnNode, new MakeDeclaredNamesUnique(new RenameVars(idSupplier, new Hashtable<>(), null, isCallInLoop)));
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
            compiler.reportChangeToEnclosingScope(nameNode);
        }
    } else {
        // ... and it is not referenced, just remove it.
        Node newChild = node.getLastChild();
        node.removeChild(nameNode);
        parent.replaceChild(node, newChild);
        if (newChild.getType() == Token.BLOCK) {
            NodeUtil.tryMergeBlock(newChild);
        }
        compiler.reportChangeToEnclosingScope(parent);
    }
    // Remove the label from the current stack of labels.
    namespaceStack.peek().renameMap().remove(name);
}