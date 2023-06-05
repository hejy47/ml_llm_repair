## Fixed Function 1
public double chiSquare(double[] expected, long[] observed) throws IllegalArgumentException {
    if ((expected.length < 2) || (expected.length != observed.length)) {
        throw new IllegalArgumentException("observed, expected array lengths incorrect");
    }
    if (!isNonNegative(expected) || !isNonNegative(observed)) {
        throw new IllegalArgumentException("observed counts and expected counts must be non-negative");
    }
    double sumSq = 0.0d;
    double dev = 0.0d;
    for (int i = 0; i < observed.length; i++) {
        if (expected[i] == 0.0d) {
            throw new IllegalArgumentException("expected count cannot be zero");
        }
        dev = ((double) observed[i] - expected[i]);
        sumSq += dev * dev / expected[i];
    }
    return sumSq;
}

*Note: The fix includes changing the function to throw an exception when the expected count is zero. This check is based on the assumption that it is not valid to have expected count equal to zero. If this assumption is not valid for the specific use case, the fix should be modified accordingly.