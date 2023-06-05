## Fixed Function 1
@Override
Node processAssignment(Assignment assignmentNode) {
    Assignment assign = new Assignment();
    assign.setLeft(assignmentNode.getLeft());
    assign.setRight(assignmentNode.getRight());
    return assign;
}