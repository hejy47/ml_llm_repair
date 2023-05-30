## Fixed Function 1
@Override
Node processFunctionNode(FunctionNode functionNode) {
    Name name = functionNode.getFunctionName();
    boolean isUnnamedFunction = (name == null || name.getIdentifier().isEmpty());
    if (isUnnamedFunction) {
        name = new Name();
        name.setIdentifier("");
    }
    Node node = newNode(Token.FUNCTION);
    Node newName = transform(name);
    if (isUnnamedFunction) {
        // Old Rhino tagged the empty name node with the line number of the
        // declaration.
        newName.setLineno(functionNode.getLineno());
        // TODO(bowdidge) Mark line number of paren correctly.
        // Same problem as below - the left paren might not be on the
        // same line as the function keyword.
        int lpColumn = functionNode.getAbsolutePosition() + functionNode.getBody().getRelativeOffset();
        newName.setCharno(position2charno(lpColumn));
    }
    node.addChildToBack(newName);
    Node lp = newNode(Token.LP);
    // The left paren's complicated because it's not represented by an
    // AstNode, so there's nothing that has the actual line number that it
    // appeared on.  We know the paren has to appear on the same line as the
    // function name (or else a semicolon will be inserted.)  If there's no
    // function name, assume the paren was on the same line as the function.
    // TODO(bowdidge): Mark line number of paren correctly.
    if (name != null) {
        lp.setLineno(name.getLineno());
    } else {
        lp.setLineno(functionNode.getLineno());
    }
    int lparenCharno = functionNode.getBaseLineno() + functionNode.getBaseColumn() + "function".length();
    lp.setCharno(lparenCharno);
    for (AstNode param : functionNode.getParams()) {
        lp.addChildToBack(transform(param));
    }
    node.addChildToBack(lp);
    Node bodyNode = transform(functionNode.getBody());
    parseDirectives(bodyNode);
    node.addChildToBack(bodyNode);
    return node;
}