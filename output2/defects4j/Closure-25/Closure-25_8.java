## Fixed Function 1
private FlowScope traverseNew(Node n, FlowScope scope) {
    Node constructor = n.getFirstChild();
    scope = traverse(constructor, scope);
    JSType constructorType = constructor.getJSType();
    JSType type = null;
    if (constructorType != null) {
        constructorType = constructorType.restrictByNotNullOrUndefined();
        if (constructorType.isUnknownType()) {
            type = getNativeType(UNKNOWN_TYPE);
        } else {
            FunctionType ct = constructorType.toMaybeFunctionType();
            if (ct == null && constructorType instanceof NoObjectType) {
                // If constructorType is a NoObjectType, then it can validly construct objects.
                ct = (FunctionType) constructorType;
            }
            if (ct != null && ct.isConstructor()) {
                type = ct.getInstanceType();
            }
        }
    }
    n.setJSType(type);
    for (Node arg = constructor.getNext(); arg != null; arg = arg.getNext()) {
        scope = traverse(arg, scope);
    }
    return scope;
}