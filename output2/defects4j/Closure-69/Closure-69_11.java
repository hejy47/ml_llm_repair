private void visitCall(NodeTraversal t, Node n) {
    Node child = n.getFirstChild();
    JSType childType = getJSType(child).restrictByNotNullOrUndefined();
    if (!childType.isFunctionType()) {
        report(t, n, NOT_CALLABLE, childType.toString());
        ensureTyped(t, n);
        return;
    }
    // A couple of types can be called as if they were functions.
    // If it is a function type, then validate parameters.
    FunctionType functionType = childType.toMaybeFunctionType();
    if (functionType == null) {
        ensureTyped(t, n);
        return;
    }
    boolean isExtern = false;
    JSDocInfo functionJSDocInfo = functionType.getJSDocInfo();
    if (functionJSDocInfo != null) {
        String sourceName = functionJSDocInfo.getSourceFileName();
        CompilerInput functionSource = compiler.getInput(sourceName);
        isExtern = functionSource != null && functionSource.isExtern();
    }
    // Non-native constructors should not be called directly
    // unless they specify a return type and are defined
    // in an extern.
    if (functionType.isConstructor() && !functionType.isNativeObjectType() && (functionType.getReturnType().isUnknownType() || functionType.getReturnType().isVoidType() || !isExtern)) {
        report(t, n, CONSTRUCTOR_NOT_CALLABLE, childType.toString());
    }
    // Functions with explicit 'this' types must be called in a GETPROP
    // or GETELEM.
    visitParameterList(t, n, functionType);
    ensureTyped(t, n, functionType.getReturnType());
    // TODO: Add something to check for calls of RegExp objects, which is not
    // supported by IE.  Either say something about the return type or warn
    // about the non-portability of the call or both.
}