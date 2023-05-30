## Fixed Function 1
private void makeLocalNamesUnique(Node fnNode, boolean isCallInLoop) {
    IdGenerator idGenerator = new UniqueNameGenerator();
    // Make variable names unique to this instance.
    NodeTraversal.traverse(compiler, fnNode, new MakeDeclaredNamesUnique(new InlineRenamer(idGenerator, "inline_", isCallInLoop)));
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
            Node newLabelNode = IR.label().setName(newName);
            while (node.hasChildren()) {
                newLabelNode.addChildToFront(node.removeFirstChild());
            }
            parent.replaceChild(node, newLabelNode);
            node = newLabelNode;
            compiler.reportCodeChange();
        }
    } else {
        // ... and it is not referenced, just remove it.
        Node newChild = node.getLastChild();
        node.removeChild(newChild);
        parent.replaceChild(node, newChild);
        if (newChild.getType() == Token.BLOCK) {
            NodeUtil.tryMergeBlock(newChild);
        }
        compiler.reportCodeChange();
    }
    // Remove the label from the current stack of labels.
    namespaceStack.peek().renameMap.remove(name);
}