private void visitNew(NodeTraversal t, Node n) {
    Node constructor = n.getFirstChild();
    JSType type = getJSType(constructor).restrictByNotNullOrUndefined();
    if (type.isConstructor() || type.isEmptyType() || type.isUnknownType()) {
        FunctionType fnType = type.toMaybeFunctionType();
        if (fnType != null) {
            visitParameterList(t, constructor, fnType); //fixed the parameter for visitParameterList function
            ensureTyped(t, n, fnType.getInstanceType());
        } else {
            ensureTyped(t, n);
        }
    } else {
        report(t, n, NOT_A_CONSTRUCTOR);
        ensureTyped(t, n);
    }
}