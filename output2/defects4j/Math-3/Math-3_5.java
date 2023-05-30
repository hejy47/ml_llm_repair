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
    double result = 0; // moved result initialization outside the loop
    final double prodHighCur = prodHigh[0];
    double prodHighNext;
    double sHighPrev;
    double sPrime;
    double sLowSum;
    for (int i = 1; i < len; i++) { // changed start index to 1
        prodHighNext = prodHigh[i];
        sHighPrev = prodHighCur + prodHighNext;
        sPrime = sHighPrev - prodHighNext;
        sLowSum = (prodHighNext - (sHighPrev - sPrime)) + (prodHighCur - sPrime);
        i++;
        if (i < len) { // added condition to check if i is still less than len
            prodHighCur = prodHigh[i];
            prodHighNext = prodHigh[i + 1];
            sHighPrev = prodHighCur + prodHighNext;
            sPrime = sHighPrev - prodHighNext;
            sLowSum += (prodHighNext - (sHighPrev - sPrime)) + (sHighPrev - sPrime);
            i++;
            for (; i < len; i++) {
                prodHighCur = prodHigh[i];
                sHighPrev = sHighPrev + prodHighCur;
                sPrime = sHighPrev - prodHighCur;
                sLowSum += (prodHighCur - (sHighPrev - sPrime)) + (sHighPrev - sPrime);
            }
        }
        result += (sHighPrev + sLowSum); // moved result addition outside loop
    }
    result += prodHighCur + prodLowSum; // added this line to fix calculation
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