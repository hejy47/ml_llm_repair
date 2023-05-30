private void visitNew(NodeTraversal t, Node n) {
    Node constructor = n.getFirstChild();
    JSType type = getJSType(constructor).restrictByNotNullOrUndefined();
    if (type.isConstructor() || type.isEmptyType() || type.isUnknownType()) {
        FunctionType fnType = type.toMaybeFunctionType();
        if (fnType != null) {
            visitParameterList(t, n, fnType);
            ensureTyped(t, n, fnType.getInstanceType());
        } else {
            ensureTyped(t, n);
        }
    } else if (type.isFunctionPrototypeObject()) { // Fix: Added check for function prototype object
        visitParameterList(t, n, null); // Fix: Pass null for fnType, as not applicable for function prototype objects
        ensureTyped(t, n, type); // Fix: Ensure type is function prototype object type, not instance type
    } else {
        report(t, n, NOT_A_CONSTRUCTOR);
        ensureTyped(t, n);
    }
}