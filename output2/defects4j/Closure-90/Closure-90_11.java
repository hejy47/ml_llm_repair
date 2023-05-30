## Fixed Function 1
@Override
public boolean apply(JSType type) {
    if (!type.isObjectType() && !type.isFunctionType()) {
        reportWarning(THIS_TYPE_NON_OBJECT, type.toString());
        return false;
    }
    return true;
}

## Fixed Function 2
@Override
JSType resolveInternal(ErrorReporter t, StaticScope<JSType> scope) {
    setResolvedTypeInternal(this);
    call = safeResolve(call, t, scope).autobox();
    prototype = safeResolve(prototype, t, scope).autobox();
    // Ensure typeOfThis is an Object type
    JSType maybeTypeOfThis = safeResolve(typeOfThis, t, scope).autobox();
    if (maybeTypeOfThis.isObjectType()) {
        typeOfThis = (ObjectType) maybeTypeOfThis;
    } else {
        typeOfThis = JSType.OBJECT_TYPE;
    }
    // Resolve implemented interfaces and subtypes
    ImmutableList.Builder<JSType> resolvedInterfaces = ImmutableList.builder();
    boolean changed = false;
    for (JSType iface : implementedInterfaces) {
        JSType resolvedIface = safeResolve(iface, t, scope).autobox();
        resolvedInterfaces.add(resolvedIface);
        changed |= (resolvedIface != iface);
    }
    if (changed) {
        implementedInterfaces = resolvedInterfaces.build();
    }
    if (subTypes != null) {
        for (int i = 0; i < subTypes.size(); i++) {
            subTypes.set(i, safeResolve(subTypes.get(i), t, scope).autobox().toMaybeFunctionType());
        }
    }
    // Resolve parent type
    return super.resolveInternal(t, scope).autobox();
}