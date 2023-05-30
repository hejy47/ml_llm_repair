## Fixed Function 1
public Dfp multiply(final int x) {
    Dfp result = newInstance(field, this);
    result.scale *= x;
    result.checkDigits10();
    return result;
}