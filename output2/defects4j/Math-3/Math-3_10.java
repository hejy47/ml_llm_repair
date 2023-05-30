public static double linearCombination(final double[] a, final double[] b) throws DimensionMismatchException {
    final int len = a.length;
    if (len != b.length) {
        throw new DimensionMismatchException(len, b.length);
    }
    // Revert to scalar multiplication.
    final double[] prodHigh = new double[len];
    double prodLowSum = 0;
    for (int i = 0; i < len; i++) {
        final double ai = a[i];
        final double ca = SPLIT_FACTOR * ai;
        final double aHigh = ca - (ca - ai);
        final double aLow = ai - aHigh;
        final double bi = b[i];
        final double cb = SPLIT_FACTOR * bi;
        final double bHigh = cb - (cb - bi);
        final double bLow = bi - bHigh;
        prodHigh[i] = ai * bi;
        final double prodLow = aLow * bLow - (((prodHigh[i] - aHigh * bHigh) - aLow * bHigh) - aHigh * bLow);
        prodLowSum += prodLow;
    }
    double sHighPrev = prodHigh[0];
    double sLowSum = 0;
    final int lenMinusOne = len - 1;
    for (int i = 1; i < lenMinusOne; i+=2) {
        final double prodHighCur = prodHigh[i];
        final double prodHighNext = prodHigh[i + 1];
        final double sHighCur = sHighPrev + prodHighCur + prodHighNext;
        final double sPrime = sHighCur - prodHighNext;
        sLowSum += (prodHighNext - (sHighCur - sPrime)) + (prodHighCur - sPrime);
        sHighPrev = sHighCur;
    }
    if ((len & 1) == 1) { // If the length of a is odd, reserve one last multiplication
        final double prodHighCur = prodHigh[len - 1];
        sHighPrev += prodHighCur;
        prodLowSum += a[len - 1] * b[len - 1];
    }
    double result = sHighPrev + (prodLowSum + sLowSum);
    if (Double.isNaN(result)) {
        // either we have split infinite numbers or some coefficients were NaNs,
        // just rely on the naive implementation and let IEEE754 handle this
        result = 0;
        for (int i = 0; i < len; ++i) {
            result += a[i] * b[i];
        }
    }
    return result;
}