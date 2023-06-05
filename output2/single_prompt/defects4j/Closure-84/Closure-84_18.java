## Fixed Function 1
@Override
Node processAssignment(Assignment assignmentNode) {
    Node assign = new Assignment(processInfixExpression(assignmentNode.getLeft()), processInfixExpression(assignmentNode.getRight()));
    return assign;
}