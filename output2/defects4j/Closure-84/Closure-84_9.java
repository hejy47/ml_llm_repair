## Fixed Function 1
@Override
Node processAssignment(Assignment assignmentNode) {
    Node assign = processExpression(assignmentNode.getValue());
    return assign;
}