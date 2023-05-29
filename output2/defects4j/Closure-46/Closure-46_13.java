## Fixed Function 1
public JSType getLeastSupertype(JSType that) {
    if (that.isUnionType()) {
        // Union types have their own implementation of getLeastSupertype.
        return that.toMaybeUnionType().getLeastSupertype(this);
    }
    if (this.equals(that)) {
        return this;
    }
    return getLeastSupertype(this, that);
}