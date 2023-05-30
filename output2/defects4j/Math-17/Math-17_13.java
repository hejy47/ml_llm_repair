## Fixed Function 1
public Dfp multiply(final int x) {
    Dfp result = newInstance(getField(), 0);
    for (int i = 0; i < x; i++) {
        result = result.add(this);
    }
    return result;
}