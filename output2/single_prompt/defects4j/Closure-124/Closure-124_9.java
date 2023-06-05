## Fixed Function 1
private boolean isSafeReplacement(Node node, Node replacement) {
    // No checks are needed for simple names.
    if (node.isName()) {
        return true;
    }
    Preconditions.checkArgument(node.isGetProp());
    node = node.getFirstChild();
    if (node.isName() && isNameAssignedTo(node.getString(), replacement)) {
        return false;
    }
    return true;
}

// Added helper function
private boolean isNameAssignedTo(String name, Node replacement) {
    // Traverse the replacement subtree to see if the name appears.
    NodeTraversal.traverse(
        compiler,
        replacement,
        new NodeTraversal.Callbacks() {
            @Override
            public boolean shouldTraverse(NodeTraversal nodeTraversal, Node node, Node parent) {
                // Only interested in simple assignments.
                return node.isAssign()
                    && node.getFirstChild().isName()
                    && node.getFirstChild().getString().equals(name);
            }

            @Override
            public void visit(NodeTraversal nodeTraversal, Node node, Node parent) {
                // If an assignment is found, break the traversal.
                throw new FoundNameAssignmentException();
            }
        });
    return false;
}

// Added helper exception for breaking traversal.
private static class FoundNameAssignmentException extends RuntimeException {}