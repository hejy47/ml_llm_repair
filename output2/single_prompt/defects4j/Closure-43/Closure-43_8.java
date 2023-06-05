## Fixed Function 1
@Override
public void visit(NodeTraversal t, Node n, Node parent) {
    inputId = t.getInputId();
    attachLiteralTypes(t, n);
    switch(n.getToken()) { // Use getToken() instead of getType()
        case CALL: // Use Token.CALL instead of Token.CALL
            checkForClassDefiningCalls(t, n, parent);
            checkForCallingConventionDefiningCalls(n, delegateCallingConventions);
            break;
        case FUNCTION: // Use Token.FUNCTION instead of Token.FUNCTION
            if (t.getInput() == null || !t.getInput().isExtern()) {
                nonExternFunctions.add(n);
            }
            // Hoisted functions are handled during pre-traversal.
            if (!NodeUtil.isHoistedFunctionDeclaration(n)) {
                defineFunctionLiteral(n, parent);
            }
            break;
        case ASSIGN: // Use Token.ASSIGN instead of Token.ASSIGN
            // Handle initialization of properties.
            Node firstChild = n.getFirstChild();
            if (firstChild.isGetProp() && firstChild.isQualifiedName()) {
                maybeDeclareQualifiedName(t, n.getJSDocInfo(), firstChild, n, firstChild.getNext());
            }
            break;
        case CATCH: // Use Token.CATCH instead of Token.CATCH
            defineCatch(n, parent);
            break;
        case VAR: // Use Token.VAR instead of Token.VAR
            defineVar(n, parent);
            break;
        case GETPROP: // Use Token.GETPROP instead of Token.GETPROP
            // Handle stubbed properties.
            if (parent.isExprResult() && n.isQualifiedName()) {
                maybeDeclareQualifiedName(t, n.getJSDocInfo(), n, parent, null);
            }
            break;
    }
    // Analyze any @lends object literals in this statement.
}

## Fixed Function 2
private void attachLiteralTypes(NodeTraversal t, Node n) {
    switch(n.getToken()) { // Use getToken() instead of getType()
        case NULL: // Use Token.NULL instead of Token.NULL
            n.setJSType(getNativeType(NULL_TYPE));
            break;
        case VOID: // Use Token.VOID instead of Token.VOID
            n.setJSType(getNativeType(VOID_TYPE));
            break;
        case STRING: // Use Token.STRING instead of Token.STRING
            // Defer keys to the Token.OBJECTLIT case
            if (!NodeUtil.isObjectLitKey(n, n.getParent())) {
                n.setJSType(getNativeType(STRING_TYPE));
            }
            break;
        case NUMBER: // Use Token.NUMBER instead of Token.NUMBER
            n.setJSType(getNativeType(NUMBER_TYPE));
            break;
        case TRUE: // Use Token.TRUE instead of Token.TRUE
        case FALSE: // Use Token.FALSE instead of Token.FALSE
            n.setJSType(getNativeType(BOOLEAN_TYPE));
            break;
        case REGEXP: // Use Token.REGEXP instead of Token.REGEX
            n.setJSType(getNativeType(REGEXP_TYPE));
            break;
        case OBJECTLIT: // Use Token.OBJECTLIT instead of Token.OBJECTLIT
            defineObjectLiteral(n);
            break;
    }
}