## Fixed Function 1
public static long binomialCoefficient(final int n, final int k) {
    if (n < k) {
        throw new IllegalArgumentException("must have n >= k for binomial coefficient (n,k)");
    }
    if (n < 0) {
        throw new IllegalArgumentException("must have n >= 0 for binomial coefficient (n,k)");
    }
    if ((n == k) || (k == 0)) {
        return 1;
    }
    if ((k == 1) || (k == n - 1)) {
        return n;
    }
    long result = 1;
    // Use symmetry for large k
    // We use the formula
    // (n choose k) = n! / (n-k)! / k!
    // (n choose k) == ((n-k+1)*...*n) / (1*...*k)
    // which could be written
    // (n choose k) == (n-1 choose k-1) * n / k
    // For n <= 61, the naive implementation cannot overflow.
    // For n > 61 but n <= 66, the result cannot overflow,
    // but we must take care not to overflow intermediate values.
    if (n <= 66) {
        int i = n - k + 1;
        for (int j = 1; j <= k; j++) {
            result = result * i / j;
            i++;
        }
    } else {
        // For n > 66, a result overflow might occur, so we use a
        // different formula: (n choose k) = exp(lgamma(n+1) - lgamma(k+1) - lgamma(n-k+1)),
        // where lgamma is the logarithm of the gamma function.
        double logResult = binomialCoefficientLog(n, k);
        if (logResult > Math.log(Long.MAX_VALUE)) {
            throw new ArithmeticException("result too large to represent in a long integer");
        }
        result = Math.round(Math.exp(logResult));
    }
    return result;
}

## Fixed Function 2
public static double binomialCoefficientDouble(final int n, final int k) {
    return Math.floor(Math.exp(binomialCoefficientLog(n, k)) + 0.5);
}

## Fixed Function 3
public static double binomialCoefficientLog(final int n, final int k) {
    if (n < k) {
        throw new IllegalArgumentException("must have n >= k for binomial coefficient (n,k)");
    }
    if (n < 0) {
        throw new IllegalArgumentException("must have n >= 0 for binomial coefficient (n,k)");
    }
    if ((n == k) || (k == 0)) {
        return 0;
    }
    if ((k == 1) || (k == n - 1)) {
        return Math.log((double) n);
    }
    double logSum = 0;
    // n!/k!
    for (int i = k + 1; i <= n; i++) {
        logSum += Math.log((double) i);
    }
    // divide by (n-k)!
    for (int i = 2; i <= n - k; i++) {
        logSum -= Math.log((double) i);
    }
    return logSum;
}