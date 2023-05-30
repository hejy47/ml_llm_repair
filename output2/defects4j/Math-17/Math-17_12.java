## Fixed Function 1
public Dfp multiply(final int x) {
    Dfp result = newInstance();
    for (int i = 0; i < field.getRadixDigits(); i++) {
        result.data[i] = (int) ((long) data[i] * (long) x);
    }
    result = result.round();
    return dotrap(result, MULTIPLY, x, result);
}