## Fixed Function 1
public Dfp multiply(final int x) {
    Dfp result = new Dfp(this);
    result.multiplyEquals(x);
    return result;
}