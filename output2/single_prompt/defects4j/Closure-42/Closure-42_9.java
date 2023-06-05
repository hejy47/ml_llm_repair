## Fixed Function 1
@Override
Node processForInLoop(ForInLoop loopNode) {
    // Return the bare minimum to put the AST in a valid state.
    return newForIn(getTreeMaker(),
                     transform(loopNode.getIterator()),
                     transform(loopNode.getIteratedObject()),
                     transformBlock(loopNode.getBody()),
                     loopNode.isForEach());
}