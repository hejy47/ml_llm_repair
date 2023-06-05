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
            ObjectType ct = constructorType.autobox().toMaybeObjectType();
            if (ct != null && ct.getConstructor() != null) {
                type = ct.getConstructor().getInstanceType();
            }
        }
    }
    n.setJSType(type);
    for (Node arg = constructor.getNext(); arg != null; arg = arg.getNext()) {
        scope = traverse(arg, scope);
    }
    return scope;
}