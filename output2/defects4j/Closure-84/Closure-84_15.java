## Fixed Function 1
@Override
Node processAssignment(Assignment assignmentNode) {
    VariableExpression var = (VariableExpression) assignmentNode.getLeft();
    Node value = processInfixExpression(assignmentNode.getRight());
    // create a new assignment node with the processed values
    return new Assignment(var, value);
}