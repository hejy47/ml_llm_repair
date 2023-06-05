## Fixed Function 1
private void makeLocalNamesUnique(Node fnNode, boolean isCallInLoop) {
    Supplier<String> idSupplier = compiler.getUniqueNameIdSupplier();
    // Make variable names unique to this instance.
    MakeDeclaredNamesUnique makeUnique = new MakeDeclaredNamesUnique(new InlineRenamer(idSupplier, "inline_", isCallInLoop));
    NodeTraversal.traverse(compiler, fnNode, makeUnique);
    // Make label names unique to this instance.
    NodeTraversal.traverse(compiler, fnNode, new LabelNameAnnotator());
    NodeTraversal.traverse(compiler, fnNode, new MakeDeclaredNamesUnique(new RenameLabelsAsUniquePrefix(makeUnique.getDefinitionMap(), "L")));
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
            // Change the node name and set type to LABEL_NAME
            nameNode.setString(newName);
            nameNode.setType(Token.LABEL_NAME);
            compiler.reportCodeChange();
        }
    } else {
        // ... and it is not referenced, just remove it.
        Node jumpTarget = node.getNext();
        if (jumpTarget == null || jumpTarget.getType() != Token.BLOCK) {
            // can't remove non-block statement or empty label
            return;
        }
        NodeUtil.tryMergeBlock(jumpTarget);
        // Replace label node with its sole child.
        parent.replaceChild(node, jumpTarget);
        compiler.reportCodeChange();
    }
    // Remove the label from the current stack of labels.
    namespaceStack.peek().renameMap.remove(name);
}