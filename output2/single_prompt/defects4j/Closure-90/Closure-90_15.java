## Fixed Function 1
@Override
public boolean apply(JSType type) {
    // TODO(user): Doing an instanceof check here is too
    // restrictive as (Date,Error) is, for instance, an object type
    // even though its implementation is a UnionType. Would need to
    // create interfaces JSType, ObjectType, FunctionType etc and have
    // separate implementation instead of the class hierarchy, so that
    // union types can also be object types, etc.
    if (!type.isSubtypeOf(typeRegistry.getNativeType(OBJECT_TYPE))) {
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
    // Warning about typeOfThis if it doesn't resolve to an ObjectType
    // is handled further upstream.
    //
    // TODO(nicksantos): Handle this correctly if we have a UnionType.
    //
    // TODO(nicksantos): In ES3, the runtime coerces "null" to the global
    // activation object. In ES5, it leaves it as null. Just punt on this
    // issue for now by coercing out null. This is complicated by the
    // fact that when most people write @this {Foo}, they really don't
    // mean "nullable Foo". For certain tags (like @extends) we de-nullify
    // the name for them.
    JSType maybeTypeOfThis = safeResolve(typeOfThis, t, scope).autobox();
    if (maybeTypeOfThis.isObjectType()) {
        typeOfThis = maybeTypeOfThis.toObjectType();
    }
    boolean changed = false;
    ImmutableList.Builder<ObjectType> resolvedInterfaces = ImmutableList.builder();
    for (ObjectType iface : implementedInterfaces) {
        ObjectType resolvedIface = (ObjectType) iface.resolve(t, scope).autobox();
        resolvedInterfaces.add(resolvedIface);
        changed |= (resolvedIface != iface);
    }
    if (changed) {
        implementedInterfaces = resolvedInterfaces.build();
    }
    if (subTypes != null) {
        for (int i = 0; i < subTypes.size(); i++) {
            subTypes.set(i, (FunctionType) subTypes.get(i).resolve(t, scope).autobox());
        }
    }
    return super.resolveInternal(t, scope);
}