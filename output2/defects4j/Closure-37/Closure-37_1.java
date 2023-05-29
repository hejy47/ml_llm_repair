## Fixed Function 1
private void traverseFunction(Node n, Node parent) {
    Preconditions.checkState(n.getChildCount() == 3);
    Preconditions.checkState(n.isFunction());
    final Node fnName = n.getFirstChild();
    boolean isFunctionExpression = (parent != null) && NodeUtil.isFunctionExpression(n);
    if (!isFunctionExpression) {
        // Functions declarations are in the scope containing the declaration.
        pushScope(n);  // push scope first
        traverseBranch(fnName, n);
        popScope();   // then pop scope
    } else {
        pushScope(n); // push scope for expression functions too
    }
    curNode = n;
    if (isFunctionExpression) {
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
    if (isFunctionExpression) {
        popScope(); // pop scope at the end for expression functions
    }
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
    if (fnName != null) {
        lp.setLineno(fnName.getLineno());
    } else {
        lp.setLineno(functionNode.getLineno());
    }
    int lparenCharno = functionNode.getLp() + functionNode.getAbsolutePosition();
    lp.setCharno(position2charno(lparenCharno));
    pushScope(lp);  // push scope for function parameters
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
    popScope();   // pop scope for function parameters
    return node;
}