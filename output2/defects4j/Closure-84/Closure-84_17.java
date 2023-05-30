## Fixed Function 1
@Override
Node processAssignment(Assignment assignmentNode) {
    Node assign = new AssignmentNode(processInfixExpression(assignmentNode.getLeft()), processInfixExpression(assignmentNode.getRight()));
    return assign;
}