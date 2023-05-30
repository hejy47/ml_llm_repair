## Fixed Function 1
private void makeLocalNamesUnique(Node fnNode, boolean isCallInLoop) {
    Supplier<String> idSupplier = compiler.getUniqueNameIdSupplier();
    // Make variable names unique to this instance.
    NodeTraversal.traverse(compiler, fnNode, new MakeDeclaredNamesUnique(new InlineRenamer(idSupplier, "inline_", isCallInLoop)));

    // Get all declared names in this function.
    Set<String> declaredNames = new HashSet<>();
    NodeTraversal.traverse(compiler, fnNode, new NodeTraversal.AbstractPostOrderCallback() {
        @Override
        public void visit(NodeTraversal t, Node n, Node parent) {
            if (n.isName()) {
                declaredNames.add(n.getString());
            }
        }
    });

    // Make label names unique to this instance, skipping names that conflict with declared names.
    NodeTraversal.traverse(compiler, fnNode, new NodeTraversal.Callback() {
        @Override
        public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
            return !n.isFunction();
        }

        @Override
        public void visit(NodeTraversal t, Node n, Node parent) {
            if (n.isLabel()) {
                String labelName = n.getFirstChild().getString();
                LabelInfo li = getLabelInfo(labelName);
                if (li.referenced) {
                    String newName = getNameForId(li.id);
                    if (!labelName.equals(newName) && !declaredNames.contains(newName)) {
                        n.getFirstChild().setString(newName);
                        compiler.reportCodeChange();
                    }
                } else {
                    n.detach();
                    compiler.reportCodeChange();
                }
            }
        }
    });
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
            // ... and it is used, give it the short name, unless it conflicts with a declared name.
            Set<String> declaredNames = new HashSet<>();
            NodeTraversal.traverse(compiler.getRoot(), new NodeTraversal.Callback() {
                @Override
                public boolean shouldTraverse(NodeTraversal t, Node n, Node parent) {
                    return !n.isFunction();
                }

                @Override
                public void visit(NodeTraversal t, Node n, Node parent) {
                    if (n.isName()) {
                        declaredNames.add(n.getString());
                    }
                }
            });
            if (!declaredNames.contains(newName)) {
                nameNode.setString(newName);
                compiler.reportCodeChange();
            }
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