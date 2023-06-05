## Fixed Function 1
JSType meet(JSType that) {
    UnionTypeBuilder builder = new UnionTypeBuilder(registry);
    for (JSType alternate : alternates) {
        if (alternate.isSubtype(that)) {
            builder.addAlternate(alternate);
        }
    }
    if (that instanceof UnionType) {
        for (JSType otherAlternate : ((UnionType) that).alternates) {
            if (this.isSubtype(otherAlternate)) {  // FIX: Swap "this" and "otherAlternate"
                builder.addAlternate(otherAlternate);
            }
        }
    } else if (this.isSubtype(that)) {  // FIX: Swap "this" and "that"
        builder.addAlternate(that);
    }
    JSType result = builder.build();
    if (result != null) {
        return result;
    } else if (this.isObject() && that.isObject()) {
        return getNativeType(JSTypeNative.NO_OBJECT_TYPE);
    } else {
        return getNativeType(JSTypeNative.NO_TYPE);
    }
}