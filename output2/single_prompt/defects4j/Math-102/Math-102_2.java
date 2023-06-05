## Fixed Function 1
public double chiSquare(double[] expected, long[] observed) throws IllegalArgumentException {
    if ((expected.length < 1) || (expected.length != observed.length)) {
        throw new IllegalArgumentException("observed, expected array lengths incorrect");
    }
    if (!isPositive(expected) || !isNonNegative(observed)) {
        throw new IllegalArgumentException("observed counts must be non-negative and expected counts must be positive");
    }
    double sumSq = 0.0d;
    double dev = 0.0d;
    for (int i = 0; i < observed.length; i++) {
        dev = ((double) observed[i] - expected[i]);
        sumSq += dev * dev / expected[i];
    }
    return sumSq;
}

The changes made include the following:
- In the first if statement, the condition was changed from "expected.length < 2" to "expected.length < 1", as it is acceptable to have an empty expected array.
- In the second if statement, the word "postive" was corrected to "positive".
- No further changes were made as the logic of the function appeared to be correct.