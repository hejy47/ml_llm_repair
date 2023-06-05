## Fixed Function 1
public static Node tryFinally(Node tryBody, Node finallyBody) {
    Preconditions.checkState(tryBody.isLabelName());
    Preconditions.checkState(finallyBody.isLabelName());
    Node catchBody = new Block().copyInformationFrom(tryBody);
    return new Node(Token.TRY, tryBody, catchBody, finallyBody);
}

## Fixed Function 2
public static Node tryCatch(Node tryBody, Node catchNode) {
    Preconditions.checkState(tryBody.isBlock());
    Preconditions.checkState(catchNode.isCatch());
    Node catchBody = new Block().copyInformationFrom(catchNode.getFirstChild().getNext());
    return new Node(Token.TRY, tryBody, catchBody);
}