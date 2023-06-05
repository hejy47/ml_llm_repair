## Fixed Function 1
@Override
Node processAssignment(Assignment assignmentNode) {
    VariableDeclarationFragment fragment = (VariableDeclarationFragment) assignmentNode.leftHandSide;
    String variableName = fragment.getName().getIdentifier();
    Node value = processExpression(assignmentNode.rightHandSide);
    return new AssignmentNode(variableName, value);
}