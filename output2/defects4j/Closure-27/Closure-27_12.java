## Fixed Function 1
public static Node tryFinally(Node tryBody, Node finallyBody) {
    Preconditions.checkState(tryBody.isBlock()); // Fixed: changed to check if tryBody is a block
    Preconditions.checkState(finallyBody.isBlock()); // Fixed: changed to check if finallyBody is a block
    Node catchBody = block().copyInformationFrom(tryBody);
    return new Node(Token.TRY, tryBody, catchBody, finallyBody);
}

## Fixed Function 2
public static Node tryCatch(Node tryBody, Node catchNode) {
    Preconditions.checkState(tryBody.isBlock());
    Preconditions.checkState(catchNode.isCatch());
    Node catchBody = catchNode.getLastChild(); // Fixed: changed to get the catch body from the last child of catchNode
    return new Node(Token.TRY, tryBody, catchBody);
}