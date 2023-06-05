## Fixed Function 1
public static Node tryFinally(Node tryBody, Node finallyBody) {
    Preconditions.checkState(tryBody.isExprResult());
    Preconditions.checkState(finallyBody.isBlock());
    Node block = IR.block().insertChildBefore(tryBody, finallyBody);
    return new Node(Token.TRY, block, null, finallyBody);
}

## Fixed Function 2
public static Node tryCatch(Node tryBody, Node catchNode) {
    Preconditions.checkState(tryBody.isBlock());
    Preconditions.checkState(catchNode.isCatch());
    Node catchBlock = IR.block(catchNode.getLastChild()).copyInformationFrom(catchNode);
    Node tryCatchNode = new Node(Token.TRY, tryBody, catchBlock);
    tryCatchNode.copyInformationFrom(tryBody);
    return tryCatchNode;
}