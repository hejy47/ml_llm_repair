## Fixed Function 1
public JSType getLeastSupertype(JSType that) {
    if (that.isUnionType()) {
        // Union types have their own implementation of getLeastSupertype.
        return that.toMaybeUnionType().getLeastSupertype(this);
    }
    // Fixed function call to use "that" as first argument instead of "this"
    return getLeastSupertype(that, this);
} 

## Fixed Function 2 (Alternative)
public JSType getLeastSupertype(JSType that) {
    if (that.isUnionType()) {
        // Union types have their own implementation of getLeastSupertype.
        return that.toMaybeUnionType().getLeastSupertype(this);
    }
    // Alternative fix: use the static method in JSType to calculate least common supertype
    return JSType.getLeastCommonSupertype(this, that);
}