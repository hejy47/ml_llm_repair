## Fixed Function 1
@Override
public boolean apply(JSType type) {
    if (!type.isObjectType()) {
        reportWarning(THIS_TYPE_NON_OBJECT, type.toString());
        return false;
    }
    return true;
}

## Fixed Function 2
@Override
JSType resolveInternal(ErrorReporter t, StaticScope<JSType> scope) {
    setResolvedTypeInternal(this);
    call = (ArrowType) call.resolve(t, scope);
    prototype = (FunctionPrototypeType) prototype.resolve(t, scope);
    JSType maybeTypeOfThis = typeOfThis.resolve(t, scope);
    if (maybeTypeOfThis.isObjectType()) {
        typeOfThis = (ObjectType) maybeTypeOfThis;
    }
    boolean changed = false;
    ImmutableList.Builder<ObjectType> resolvedInterfaces = ImmutableList.builder();
    for (ObjectType iface : implementedInterfaces) {
        ObjectType resolvedIface = (ObjectType) iface.resolve(t, scope);
        resolvedInterfaces.add(resolvedIface);
        changed |= (resolvedIface != iface);
    }
    if (changed) {
        implementedInterfaces = resolvedInterfaces.build();
    }
    if (subTypes != null) {
        for (int i = 0; i < subTypes.size(); i++) {
            subTypes.set(i, (FunctionType) subTypes.get(i).resolve(t, scope));
        }
    }
    return super.resolveInternal(t, scope);
}