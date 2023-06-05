private void recordAssignment(NodeTraversal t, Node n, Node recordNode) {
    Node nameNode = n.getFirstChild();
    Node parent = n.getParent();
    NameInformation ns = createNameInformation(t, nameNode);
    if (ns != null) {
        if (parent.isFor() && !NodeUtil.isForIn(parent)) {

            // Check if the assignment is in the initialization part of FOR loop
            if (parent.getFirstChild() == n) {
                recordDepScope(recordNode, ns);
            } else {
                // For assignments in condition and iteration part, only the last
                // assignment should be considered for dependency scope
                Node prevSibling = n.getPrevious();
                while (prevSibling != null && NodeUtil.isAssignmentOp(prevSibling)) {
                    prevSibling = prevSibling.getPrevious();
                }
                if (prevSibling != null) {
                    Node prevNameNode = prevSibling.getFirstChild();
                    NameInformation prevNs = createNameInformation(t, prevNameNode);
                    if (prevNs != null) {
                        recordDepScope(prevNameNode, prevNs);
                    }
                }
                recordDepScope(nameNode, ns);
            }
        } else {
            // The rhs of the assignment is the caller, so it's used by the
            // context. Don't associate it w/ the lhs.
            // FYI: this fixes only the specific case where the assignment is the
            // caller expression, but it could be nested deeper in the caller and
            // we would still get a bug.
            // See testAssignWithCall2 for an example of this.
            recordDepScope(recordNode, ns);
        }
    }
}