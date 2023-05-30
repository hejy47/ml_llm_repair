## Fixed Function 1
public Dfp multiply(final int x) {
    Dfp result = newInstance(this);
    for (int i = 1; i < x; i++) {
        result = result.add(this);
    }
    return result;
}