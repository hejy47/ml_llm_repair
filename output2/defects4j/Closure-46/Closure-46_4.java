## Fixed Function 1
public JSType getLeastSupertype(JSType that) {
    if (that.isUnionType()) {
        // Union types have their own implementation of getLeastSupertype.
        return that.toMaybeUnionType().getLeastSupertype(this);
    }
    return getLeastSupertypeHelper(this, that);
}

private JSType getLeastSupertypeHelper(JSType type1, JSType type2) {
    if (type1.equals(type2)) {
        return type1;
    }
    if (type1.isUnknownType() || type2.isUnknownType()) {
        return JSType.UNKNOWN;
    }
    if (type1.isAnyType() || type2.isAnyType()) {
        return JSType.ANY;
    }
    if (type1.isTopType()) {
        return type2;
    }
    if (type2.isTopType()) {
        return type1;
    }
    if (type1.isNumberType() && type2.isNumberType()) {
        return JSType.NUMBER;
    }
    if (type1.isStringType() && type2.isStringType()) {
        return JSType.STRING;
    }
    if (type1.isBooleanType() && type2.isBooleanType()) {
        return JSType.BOOLEAN;
    }
    if (type1.isObjectType() && type2.isObjectType()) {
        ObjectType obj1 = type1.toMaybeObjectType();
        ObjectType obj2 = type2.toMaybeObjectType();
        return ObjectType.join(obj1, obj2);
    }
    return JSType.TOP_OBJECT;
}