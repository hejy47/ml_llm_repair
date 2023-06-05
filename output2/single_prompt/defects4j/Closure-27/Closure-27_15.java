## Fixed Function 1
public static Node tryFinally(Node tryBody, Node finallyBody) {
    Preconditions.checkState(!finallyBody.isEmpty());
    Preconditions.checkState(tryBody.isBlock());
    return new Node(Token.TRY, tryBody, null, finallyBody);
}

## Fixed Function 2
public static Node tryCatch(Node tryBody, Node catchNode) {
    Preconditions.checkState(tryBody.isBlock());
    Preconditions.checkState(catchNode.isCatch());
    Node catchBody = catchNode.getLastChild();
    return new Node(Token.TRY, tryBody, catchNode, catchBody);
}