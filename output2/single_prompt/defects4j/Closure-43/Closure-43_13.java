## Fixed Function 1
@Override
public void visit(NodeTraversal t, Node n, Node parent) {
    inputId = t.getInputId();
    attachLiteralTypes(n); // Changed to call attachLiteralTypes with only the node as argument
    switch(n.getType()) {
        case Token.CALL:
            checkForClassDefiningCalls(t, n, parent);
            checkForCallingConventionDefiningCalls(n, delegateCallingConventions);
            break;
        case Token.FUNCTION:
            if (t.getInput() == null || !t.getInput().isExtern()) {
                nonExternFunctions.add(n);
            }
            // Hoisted functions are handled during pre-traversal.
            if (!NodeUtil.isHoistedFunctionDeclaration(n)) {
                defineFunctionLiteral(n, parent);
            }
            break;
        case Token.ASSIGN:
            // Handle initialization of properties.
            Node firstChild = n.getFirstChild();
            if (firstChild.isGetProp() && firstChild.isQualifiedName()) {
                maybeDeclareQualifiedName(t, n.getJSDocInfo(), firstChild, n, firstChild.getNext());
            }
            break;
        case Token.CATCH:
            defineCatch(n, parent);
            break;
        case Token.VAR:
            defineVar(n, parent);
            break;
        case Token.GETPROP:
            // Handle stubbed properties.
            if (parent.isExprResult() && n.isQualifiedName()) {
                maybeDeclareQualifiedName(t, n.getJSDocInfo(), n, parent, null);
            }
            break;
    }
    // Analyze any @lends object literals in this statement.
}

## Fixed Function 2
private void attachLiteralTypes(Node n) { // Removed the NodeTraversal argument
    switch(n.getType()) {
        case Token.NULL:
            n.setJSType(getNativeType(NULL_TYPE));
            break;
        case Token.VOID:
            n.setJSType(getNativeType(VOID_TYPE));
            break;
        case Token.STRING:
            // Defer keys to the Token.OBJECTLIT case
            if (!NodeUtil.isObjectLitKey(n, n.getParent())) {
                n.setJSType(getNativeType(STRING_TYPE));
            }
            break;
        case Token.NUMBER:
            n.setJSType(getNativeType(NUMBER_TYPE));
            break;
        case Token.TRUE:
        case Token.FALSE:
            n.setJSType(getNativeType(BOOLEAN_TYPE));
            break;
        case Token.REGEXP:
            n.setJSType(getNativeType(REGEXP_TYPE));
            break;
        case Token.OBJECTLIT:
            defineObjectLiteral(n);
            break;
    }
}