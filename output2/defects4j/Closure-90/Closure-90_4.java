## Fixed Function 1
@Override
public boolean apply(JSType type) {
    // Check if type is an object or union type containing object type(s)
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
    call = safeResolve(call, t, scope).autobox();
    prototype = safeResolve(prototype, t, scope).autobox();
    // Check if typeOfThis is an object or union type containing object type(s)
    JSType maybeTypeOfThis = safeResolve(typeOfThis, t, scope);
    if (maybeTypeOfThis.isObjectType()) {
        typeOfThis = (ObjectType) maybeTypeOfThis;
    } else if (maybeTypeOfThis.isUnionType()) {
        for (JSType alt : maybeTypeOfThis.toMaybeUnionType().getAlternates()) {
            if (alt.isObjectType()) {
                typeOfThis = (ObjectType) alt;
                break;
            }
        }
        if (!typeOfThis.isObjectType()) {
            t.report(typeOfThis.getSourceName(), getDeclaringType().getNode(),
                    MAYBE_NON_OBJECT_TYPEOFTHIS, typeOfThis.toString());
            typeOfThis = getCommonTypes().getUnknownType();
        }
    } else {
        t.report(typeOfThis.getSourceName(), getDeclaringType().getNode(),
                MAYBE_NON_OBJECT_TYPEOFTHIS, typeOfThis.toString());
        typeOfThis = getCommonTypes().getUnknownType();
    }
    boolean changed = false;
    ImmutableList.Builder<ObjectType> resolvedInterfaces = ImmutableList.builder();
    for (ObjectType iface : implementedInterfaces) {
        ObjectType resolvedIface = iface.resolvedType();
        resolvedInterfaces.add(resolvedIface);
        changed |= (resolvedIface != iface);
    }
    if (changed) {
        implementedInterfaces = resolvedInterfaces.build();
    }
    if (subTypes != null) {
        for (int i = 0; i < subTypes.size(); i++) {
            subTypes.set(i, subTypes.get(i).resolvedType());
        }
    }
    return super.resolveInternal(t, scope);
}