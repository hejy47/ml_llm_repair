## Fixed Function 1
private void traverse(Node node) {
    if (!shouldVisit(node)) {
        return;
    }
    int visits = 0;
    Set<Node> visitedNodes = new HashSet<>(); // introduce a set to keep track of visited nodes
    do {
        Node c = node.getFirstChild();
        while (c != null) {
            if (!visitedNodes.contains(c)) { // check if the child node has already been visited
                traverse(c);
                visitedNodes.add(c); // mark the child node as visited
            }
            Node next = c.getNext();
            c = next;
        }
        visit(node);
        visits++;
        Preconditions.checkState(visits < 10000, "too many iterations");
    } while (shouldRetraverse(node));
    exitNode(node);
}