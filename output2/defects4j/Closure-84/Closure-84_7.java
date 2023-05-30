## Fixed Function 1
@Override
Node processAssignment(Assignment assignmentNode) {
    Node assign = processInfixExpression(assignmentNode.getRight());
    return assign;
}