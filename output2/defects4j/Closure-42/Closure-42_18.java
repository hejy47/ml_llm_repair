## Fixed Function 1
@Override
Node processForInLoop(ForInLoop loopNode) {
    // Wrap the transformed body in a block if it isn't already.
    Node body = loopNode.getBody();
    if (!(body instanceof Block)) {
        body = new Block(new Node[]{body});
    }
    // Return the transformed for-in loop.
    return new Node(Token.FOR,
                    transform(loopNode.getIterator()),
                    transform(loopNode.getIteratedObject()),
                    body);
}