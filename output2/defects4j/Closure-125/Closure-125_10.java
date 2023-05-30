private void visitNew(NodeTraversal t, Node n) {
    Node constructor = n.getFirstChild();
    JSType type = getJSType(constructor).restrictByNotNullOrUndefined();
    if (type.isConstructor() || type.isEmptyType() || type.isUnknownType()) {
        FunctionType fnType = type.toMaybeFunctionType();
        if (fnType != null) {
            visitParameterList(t, n, fnType);
            ensureTyped(t, constructor, fnType.getInstanceType());
        } else {
            ensureTyped(t, constructor);
        }
    } else {
        report(t, constructor, NOT_A_CONSTRUCTOR);
        ensureTyped(t, constructor);
    }
}