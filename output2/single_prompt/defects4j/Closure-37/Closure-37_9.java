## Fixed Function 1
private void traverseFunction(Node n, Node parent) {
    Preconditions.checkState(n.hasChildren());
    Preconditions.checkState(n.getChildCount() == 3);
    Preconditions.checkState(n.isFunction());
    final Node fnName = n.getFirstChild();
    boolean isFunctionExpression = (parent != null) && NodeUtil.isFunctionExpression(n);
    if (!isFunctionExpression) {
        // Functions declarations are in the scope containing the declaration.
        traverseBranch(fnName);
    }
    curNode = n;
    pushScope(n);
    if (isFunctionExpression) {
        // Function expression names are only accessible within the function
        // scope.
        traverseBranch(fnName);
    }
    final Node args = fnName.getNext();
    final Node body = args.getNext();
    // Args
    if (args.hasChildren()) {
        traverseBranch(args);
    }
    // Body
    Preconditions.checkState(body.getNext() == null && body.isBlock());
    if (body.hasChildren()){
        traverseBranch(body);
    }
    popScope();
}

## Fixed Function 2
@Override
Node processFunctionNode(FunctionNode functionNode) {
    Name name = functionNode.getFunctionName();
    Boolean isUnnamedFunction = false;
    if (name == null || isBlankName(name)) {
        int functionType = functionNode.getFunctionType();
        if (functionType != FunctionNode.FUNCTION_EXPRESSION) {
            errorReporter.error("unnamed function statement", sourceName, functionNode.getLineno(), "", 0);
            // Return the bare minimum to put the AST in a valid state.
            return newNode(Token.EMPTY, functionNode.getLineno());
        }
        name = new Name();
        name.setIdentifier("");
        isUnnamedFunction = true;
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
        int lpColumn = functionNode.getAbsolutePosition() + functionNode.getLp();
        newName.setCharno(position2charno(lpColumn));
        maybeSetLengthFrom(newName, name);
    }
    node.addChildToBack(newName);
    Node lp = newNode(Token.PARAM_LIST);
    // The left paren's complicated because it's not represented by an
    // AstNode, so there's nothing that has the actual line number that it
    // appeared on.  We know the paren has to appear on the same line as the
    // function name (or else a semicolon will be inserted.)  If there's no
    // function name, assume the paren was on the same line as the function.
    // TODO(bowdidge): Mark line number of paren correctly.
    Name fnName = functionNode.getFunctionName();
    int parentLineno = functionNode.getLineno();
    if (fnName != null) {
        lp.setLineno(fnName.getLineno());
        parentLineno = fnName.getLineno();
    } else {
        lp.setLineno(parentLineno);
    }
    int lparenCharno = functionNode.getLp() + functionNode.getAbsolutePosition();
    lp.setCharno(position2charno(lparenCharno));
    for (AstNode param : functionNode.getParams()) {
        lp.addChildToBack(transform(param));
    }
    node.addChildToBack(lp);
    Node bodyNode = transform(functionNode.getBody());
    // When in ideMode Rhino tries to parse some constructs the compiler
    // doesn't support, repair it here. see Rhino's
    // Parser#parseFunctionBodyExpr.
    parseDirectives(bodyNode);
    node.addChildToBack(bodyNode);
    node.setLineno(parentLineno);
    return node;
}

# Helper Function

private boolean isBlankName(Name name) {
    return name.getIdentifier() == null || name.getIdentifier().trim().isEmpty();
}