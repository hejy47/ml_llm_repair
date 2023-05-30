## Fixed Function 1
private void makeLocalNamesUnique(Node fnNode, boolean isCallInLoop) {
    Supplier<String> idSupplier = compiler.getUniqueNameIdSupplier();
    // Make variable names unique to this instance.
    RenameDeclaredVariables uniqueRename = new RenameDeclaredVariables(idSupplier, new SymbolTable(), isCallInLoop);
    NodeTraversal.traverse(compiler, fnNode, uniqueRename);
    // Make label names unique to this instance.
    MakeDeclaredNamesUnique labelRename = new MakeDeclaredNamesUnique(new InlineRenamer(idSupplier, "inline_", isCallInLoop));
    NodeTraversal.traverse(compiler, fnNode, labelRename);
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
            Node newNameNode = nameNode.cloneTree();
            newNameNode.setString(newName);
            node.replaceChild(nameNode, newNameNode);
            compiler.reportCodeChange();
        }
    } else {
        // ... and it is not referenced, just remove it.
        parent.removeChild(node);
        if (node.getLastChild().getType() == Token.BLOCK) {
            NodeUtil.tryMergeBlock(node.getLastChild());
        }
        compiler.reportCodeChange();
    }
    // Remove the label from the current stack of labels.
    namespaceStack.peek().renameMap.remove(name);
}