## Fixed Function 1
@Override
Node processAssignment(Assignment assignmentNode) {
    Node assign = new AssignNode(processExpression(assignmentNode.getValue()), processExpression(assignmentNode.getTarget()));
    return assign;
}