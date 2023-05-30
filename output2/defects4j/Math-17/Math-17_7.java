## Fixed Function 1
public Dfp multiply(final int x) {
    Dfp result = newInstance();
    for (int i = 1; i <= x; i++) {
        result = result.add(this);
    }
    return result;
}