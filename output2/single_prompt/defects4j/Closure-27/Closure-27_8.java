## Fixed Function 1
public static Node tryFinally(Node tryBody, Node finallyBody) {
    Preconditions.checkState(tryBody.isBlock());
    Preconditions.checkState(finallyBody.isBlock());
    Node tryCatchBody = new Node(Token.TRY, tryBody, null, null).srcref(tryBody);
    Node tryCatchFinallyBody = new Node(Token.TRY, tryCatchBody, null, finallyBody);
    return tryCatchFinallyBody;
}

## Fixed Function 2
public static Node tryCatch(Node tryBody, Node catchNode) {
    Preconditions.checkState(tryBody.isBlock());
    Preconditions.checkState(catchNode.isCatch());
    Node catchBody = catchNode.getLastChild().detach();
    Node tryCatchBody = new Node(Token.TRY, tryBody, null, catchBody).srcref(tryBody);
    Node tryCatchFinallyBody = new Node(Token.BLOCK, tryCatchBody, catchNode);
    return tryCatchFinallyBody;
}