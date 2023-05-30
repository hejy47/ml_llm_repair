## Fixed Function 1
@Override
Node processAssignment(Assignment assignmentNode) {
    Node assign = new Node();
    assign.setType(NodeType.ASSIGNMENT);
    assign.setLeft(assignmentNode.getTarget());
    assign.setRight(processInfixExpression(assignmentNode.getValue()));
    return assign;
}