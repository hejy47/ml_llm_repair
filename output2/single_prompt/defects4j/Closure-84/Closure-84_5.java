## Fixed Function 1
@Override
Node processAssignment(Assignment assignmentNode) {
    Node assign = new Assignment(processExpression(assignmentNode.getTarget()), processExpression(assignmentNode.getValue()));
    return assign;
}