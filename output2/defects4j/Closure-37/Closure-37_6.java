## Fixed Function 1
private void traverseFunction(Node n, Node parent) {
    Preconditions.checkState(n.getChildCount() == 3);
    Preconditions.checkState(NodeUtil.isFunctionDeclaration(n) || NodeUtil.isFunctionExpression(n));
    final Node fnName = n.getFirstChild();
    if (NodeUtil.isFunctionDeclaration(n)) {
        // Functions declarations are in the scope containing the declaration.
        traverseBranch(fnName, n);
    }
    curNode = n;
    pushScope(n);
    if (NodeUtil.isFunctionExpression(n)) {
        // Function expression names are only accessible within the function
        // scope.
        traverseBranch(fnName, n);
    }
    final Node args = fnName.getNext();
    final Node body = args.getNext();
    // Args
    traverseBranch(args, n);
    // Body
    Preconditions.checkState(body.getNext() == null && body.isBlock());
    traverseBranch(body, n);
    popScope();
}

## Fixed Function 2
@Override
Node processFunctionNode(FunctionNode functionNode) {
    Name name = functionNode.getFunctionName();
    Boolean isUnnamedFunction = false;
    if (name == null) {
        int functionType = functionNode.getFunctionType();
        if (functionType != FunctionNode.FUNCTION_EXPRESSION) {
            errorReporter.error("unnamed function statement", sourceName, functionNode.getLineno(), "", 0);
            // Return the bare minimum to put the AST in a valid state.
            return newNode(Token.EXPR_RESULT, Node.newNumber(0));
        }
        name = new Name();
        name.setIdentifier("");
        isUnnamedFunction = true;
    }
    Node node = newNode(Token.FUNCTION);
    Node newName = transform(name);
    if (isUnnamedFunction) {
        newName.setLineno(functionNode.getLineno());
        // Get the character position of the left parenthesis instead of the identifier
        int lpColumn = functionNode.fn.getToken().getCharPosition() + functionNode.fn.getLength();
        newName.setCharno(lpColumn);
        maybeSetLengthFrom(newName, name);
    }
    node.addChildToBack(newName);
    Node lp = newNode(Token.PARAM_LIST);
    lp.setLineno(functionNode.getLineno());
    int lparenCharno = -1;
    for (AstNode param : functionNode.getParams()) {
        lp.addChildToBack(transform(param));
        if (lparenCharno == -1) {
            lparenCharno = param.getPosition() + param.getLength();
        }
    }
    lp.setCharno(lparenCharno);
    node.addChildToBack(lp);
    Node bodyNode = transform(functionNode.getBody());
    // When in ideMode Rhino tries to parse some constructs the compiler
    // doesn't support, repair it here. see Rhino's
    // Parser#parseFunctionBodyExpr.
    parseDirectives(bodyNode, functionNode.getFunctionType());
    node.addChildToBack(bodyNode);
    return node;
}