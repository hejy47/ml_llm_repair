## Fixed Function 1
@Override
public void visit(NodeTraversal t, Node n, Node parent) {
    inputId = t.getInputId();
    attachLiteralTypes(t, n);
    switch(n.getToken()) { // Use getToken() instead of getType()
        case CALL: // Use enum constant instead of Token value
            checkForClassDefiningCalls(t, n, parent);
            checkForCallingConventionDefiningCalls(n, delegateCallingConventions);
            break;
        case FUNCTION: // Use enum constant instead of Token value
            if (t.getInput() == null || !t.getInput().isExtern()) {
                nonExternFunctions.add(n);
            }
            // Hoisted functions are handled during pre-traversal.
            if (!NodeUtil.isHoistedFunctionDeclaration(n)) {
                defineFunctionLiteral(n, parent);
            }
            break;
        case ASSIGN: // Use enum constant instead of Token value
            // Handle initialization of properties.
            Node firstChild = n.getFirstChild();
            if (firstChild.isGetProp() && firstChild.isQualifiedName()) {
                maybeDeclareQualifiedName(t, n.getJSDocInfo(), firstChild, n, firstChild.getNext());
            }
            break;
        case CATCH: // Use enum constant instead of Token value
            defineCatch(n, parent);
            break;
        case VAR: // Use enum constant instead of Token value
            defineVar(n, parent);
            break;
        case GETPROP: // Use enum constant instead of Token value
            // Handle stubbed properties.
            if (parent.isExprResult() && n.isQualifiedName()) {
                maybeDeclareQualifiedName(t, n.getJSDocInfo(), n, parent, null);
            }
            break;
        case OBJECTLIT: // Use enum constant instead of Token value
            // Analyze any @lends object literals in this statement.
            defineObjectLiteral(n);
            break;
    }
}

## Fixed Function 2
private void attachLiteralTypes(NodeTraversal t, Node n) {
    switch(n.getToken()) { // Use getToken() instead of getType()
        case NULL: // Use enum constant instead of Token value
            n.setJSType(getNativeType(NULL_TYPE));
            break;
        case VOID: // Use enum constant instead of Token value
            n.setJSType(getNativeType(VOID_TYPE));
            break;
        case STRING: // Use enum constant instead of Token value
            // Defer keys to the Token.OBJECTLIT case
            if (!NodeUtil.isObjectLitKey(n, n.getParent())) {
                n.setJSType(getNativeType(STRING_TYPE));
            }
            break;
        case NUMBER: // Use enum constant instead of Token value
            n.setJSType(getNativeType(NUMBER_TYPE));
            break;
        case TRUE:
        case FALSE:
            n.setJSType(getNativeType(BOOLEAN_TYPE));
            break;
        case REGEXP:
            n.setJSType(getNativeType(REGEXP_TYPE));
            break; 
        case OBJECTLIT: // Use enum constant instead of Token value
            defineObjectLiteral(n);
            break;
    }
}