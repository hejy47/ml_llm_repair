## Fixed Function 1
private void makeLocalNamesUnique(Node fnNode, boolean isCallInLoop) {
    Supplier<String> idSupplier = compiler.getUniqueNameIdSupplier();
    // Make variable names unique to this instance.
    MakeDeclaredNamesUnique renamer1 = new MakeDeclaredNamesUnique(idSupplier);
    InlineRenamer renamer2 = new InlineRenamer(idSupplier, "inline_", isCallInLoop);
    NodeTraversal.traverse(compiler, fnNode, new NodeTraversal.AbstractPostOrderCallback() {
        @Override
        public void visit(NodeTraversal t, Node n, Node parent) {
            if (n.isParamList()) {
                // Do not rename parameters.
                return;
            }
            renamer1.visit(n, parent);
            renamer2.visit(n, parent);
        }
    });
    // Make label names unique to this instance.
    new LabelNameAnnotator().process(compiler, fnNode);
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
            // Recompute the label info, as it depends on label names.
            computeLabelInfo();
        }
    } else {
        // ... and it is not referenced, just remove it.
        NodeUtil.removeChild(parent, node);
        compiler.reportCodeChange();
        // Recompute the label info, as it depends on label names.
        computeLabelInfo();
    }
    // Remove the label from the current stack of labels.
    LabelInfo removed = namespaceStack.peek().labels.remove(name);
    Preconditions.checkState(removed != null);
}