## Fixed Function 1
@Override
Node processAssignment(Assignment assignmentNode) {
    Identifier identifierNode = (Identifier) assignmentNode.getLeft();
    Node assignmentValueNode = processInfixExpression(assignmentNode.getRight());
    return new Assignment(identifierNode, assignmentValueNode);
}