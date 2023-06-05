## Fixed Function 1
public double chiSquare(double[] expected, long[] observed) throws IllegalArgumentException {
    if ((expected.length < 1) || (expected.length != observed.length)) {
        throw new IllegalArgumentException("observed, expected array lengths incorrect");
    }
    if (!isPositive(expected) || !isNonNegative(observed)) {
        throw new IllegalArgumentException("observed counts must be non-negative and expected counts must be positive");
    }
    double sumSq = 0.0d;
    for (int i = 0; i < observed.length; i++) {
        double dev = ((double) observed[i] - expected[i]);
        sumSq += dev * dev / expected[i];
    }
    return sumSq;
}

### Changes Made
* Changed the first condition in line 2 from (expected.length < 2) to (expected.length < 1) because the expected array must have at least one element.
* Fixed a typo in the second condition in line 4 from "postive" to "positive".
* Moved the declaration of the "dev" variable inside the loop (line 9) to avoid reusing its value in the next iteration.