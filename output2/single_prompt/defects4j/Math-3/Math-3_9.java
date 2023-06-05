public static double linearCombination(final double[] a, final double[] b) throws DimensionMismatchException {
    final int len = a.length;
    if (len != b.length) {
        throw new DimensionMismatchException(len, b.length);
    }
    // Revert to scalar multiplication.
    double prodHighSum = 0;
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
        double prodHigh = ai * bi;
        final double prodLow = aLow * bLow - (((prodHigh - aHigh * bHigh) - aLow * bHigh) - aHigh * bLow);
        prodHighSum += prodHigh;
        prodLowSum += prodLow;
    }
    double result = FastMath.scalb(prodHighSum, -27) + FastMath.scalb(prodLowSum, -53);
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