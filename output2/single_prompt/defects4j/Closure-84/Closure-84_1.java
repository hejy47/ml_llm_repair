## Fixed Function 1
@Override
Node processAssignment(Assignment assignmentNode) {
    Node leftHandSide = processExpression(assignmentNode.left());
    Node rightHandSide = processExpression(assignmentNode.right());
    return new AssignmentNode(leftHandSide, rightHandSide);
}