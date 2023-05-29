## Fixed Function 1
public static Node tryFinally(Node tryBody, Node finallyBody) {
    Preconditions.checkState(tryBody.isBlock());
    Preconditions.checkState(finallyBody.isBlock());
    Node catchBody = block();
    return new Node(Token.TRY, tryBody, catchBody, finallyBody);
}

## Fixed Function 2
public static Node tryCatch(Node tryBody, Node catchNode) {
    Preconditions.checkState(tryBody.isBlock());
    Preconditions.checkState(catchNode.isCatchBlock());
    Node catchBody = catchNode.getLastChild();
    return new Node(Token.TRY, tryBody, catchBody);
}