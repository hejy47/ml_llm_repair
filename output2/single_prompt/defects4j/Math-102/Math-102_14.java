## Fixed Function 1
public double chiSquare(double[] expected, long[] observed) throws IllegalArgumentException {
    if ((expected.length < 2) || (expected.length != observed.length)) {
        throw new IllegalArgumentException("observed, expected array lengths incorrect");
    }
    if (!isPositive(expected) || !isNonNegative(observed)) {
        throw new IllegalArgumentException("observed counts must be non-negative and expected counts must be positive");
    }
    double sumSq = 0.0d;
    for (int i = 0; i < observed.length; i++) {
        double dev = observed[i] - expected[i];
        sumSq += dev * dev / expected[i];
    }
    return sumSq;
} 

Note: The fix includes changing "postive" to "positive" and moving the declaration of "dev" inside the loop. This fixes a bug where the same variable "dev" is reused across iterations of the loop and accumulates values incorrectly.