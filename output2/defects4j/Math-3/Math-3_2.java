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
    double result;
    try {
        final TwoProduct tp = TwoProduct.twoProduct(prodHigh[0], prodHigh[1]);
        result = tp.first + tp.second;
        double sPrime = tp.second, sHighPrev = result, sLowSum = 0;
        for (int i = 1; i < len - 1; i++) {
            tp.times(prodHigh[i + 1]);
            double sHighCur = sHighPrev + tp.first;
            sLowSum += (tp.second - sPrime) + (sHighPrev - (sHighCur - tp.first));
            sPrime = tp.second;
            sHighPrev = sHighCur;
        }
        tp.times(prodLowSum);
        result += tp.first + tp.second + sHighPrev + sLowSum;
    } catch (ArithmeticException e) {
        // we have split infinite numbers or some coefficients were NaNs,
        // just rely on the naive implementation and let IEEE754 handle this
        result = 0;
        for (int i = 0; i < len; ++i) {
            result += a[i] * b[i];
        }
    }
    return result;
}