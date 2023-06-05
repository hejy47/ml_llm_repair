## Fixed Function 1
private void traverse(Node node) {
    // The goal here is to avoid retraversing
    // the entire AST to catch newly created opportunities.
    // So we track whether a "unit of code" has changed,
    // and revisit immediately.
    int visits = 0;
    while (shouldVisit(node) && shouldRetraverse(node)) {
        Node c = node.getFirstChild();
        while (c != null) {
            traverse(c);
            Node next = c.getNext();
            c = next;
        }
        visit(node);
        visits++;
        Preconditions.checkState(visits < 10000, "too many iterations");
    }
    exitNode(node);
}