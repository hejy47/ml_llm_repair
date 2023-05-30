private void visitNew(NodeTraversal t, Node n) {
    Node constructor = n.getFirstChild();
    JSType type = getJSType(constructor).restrictByNotNullOrUndefined();
    if (type.isConstructor() || type.isEmptyType() || type.isUnknownType()) {
        FunctionType fnType = type.toMaybeFunctionType();
        if (fnType != null) {
            visitParameterList(t, n, fnType);
            ensureTyped(t, n, fnType.getInstanceType());
        } else {
            ensureTyped(t, n, type); // Fix: pass 'type' instead of 'null' to ensureTyped when fnType is null
        }
    } else {
        report(t, n, NOT_A_CONSTRUCTOR);
        ensureTyped(t, n, type); // Fix: pass 'type' instead of 'null' to ensureTyped when the type is not a constructor or function
    }
}