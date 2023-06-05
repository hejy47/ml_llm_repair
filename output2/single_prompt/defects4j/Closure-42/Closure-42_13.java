## Fixed Function 1
@Override
Node processForInLoop(ForInLoop loopNode) {
    // Return the bare minimum to put the AST in a valid state.
    Node lhs = loopNode.getIterator();
    Node obj = loopNode.getIteratedObject();

    lhs.setShouldBeJsonObject();
    obj.setShouldBeJsonObject();

    return newNode(Token.FOR, lhs, obj, transformBlock(loopNode.getBody()));
}