## Fixed Function 1
public static Node tryFinally(Node tryBody, Node finallyBody) {
    Preconditions.checkState(tryBody.isBlock()); // changed from isLabelName to isBlock
    Preconditions.checkState(finallyBody.isBlock()); // changed from isLabelName to isBlock
    Node catchBody = block().copyInformationFrom(tryBody);
    return new Node(Token.TRY, tryBody, catchBody, finallyBody);
}

## Fixed Function 2
public static Node tryCatch(Node tryBody, Node catchNode) {
    Preconditions.checkState(tryBody.isBlock());
    Preconditions.checkState(catchNode.isParamList() && catchNode.hasMoreThanOneChild()); // changed from isCatch to isParamList and hasMoreThanOneChild
    Node catchBody = block(catchNode.getLastChild()).copyInformationFrom(catchNode.getLastChild()); // changed from catchNode to catchNode.getLastChild()
    return new Node(Token.TRY, tryBody, catchBody);
}