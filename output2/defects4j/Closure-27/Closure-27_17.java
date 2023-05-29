## Fixed Function 1
public static Node tryFinally(Node tryBody, Node finallyBody) {
    Preconditions.checkState(tryBody.isBlock() || tryBody.isLabelName());
    Preconditions.checkState(finallyBody.isBlock() || finallyBody.isLabelName());
    Node catchBody = block().copyInformationFrom(tryBody);
    return new Node(Token.TRY, tryBody, catchBody, finallyBody);
}

## Fixed Function 2
public static Node tryCatch(Node tryBody, Node catchNode) {
    Preconditions.checkState(tryBody.isBlock());
    Preconditions.checkState(catchNode.isCatch() && catchNode.hasChildren());
    Node catchBody = block(catchNode.getLastChild()).copyInformationFrom(catchNode);
    return new Node(Token.TRY, tryBody, catchBody);
}