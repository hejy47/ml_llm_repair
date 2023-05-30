## Fixed Function 1
@Override
Node processAssignment(Assignment assignmentNode) {
    Node assign = new Node();
    assign.left = processIdentifier(assignmentNode.getTarget());
    assign.type = Node.Type.ASSIGNMENT;
    assign.right = processExpression(assignmentNode.getValue());
    return assign;
}