## Fixed Function 1
@Override
Node processAssignment(Assignment assignmentNode) {
    Node assign = new Node(ASSIGN, assignmentNode);
    Node exp = processInfixExpression(assignmentNode);
    assign.addChild(exp);
    return assign;
}