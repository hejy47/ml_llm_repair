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
    if (k > n - k) {
        // Use symmetry for large k
        // We use the formula
        // (n choose k) = n! / (n-k)! / k!
        // (n choose k) == ((n-k+1)*...*n) / (1*...*k)
        // which could be written
        // (n choose k) == (n-1 choose k-1) * n / k
        // Calculate (n-1 choose k-1) instead of (n choose k)
        result = binomialCoefficient(n - 1, k - 1);
        // Multiply by n / k
        result = (result * n) / k;
    } else {
        // Calculate (n-1 choose k) instead of (n choose k)
        result = binomialCoefficient(n - 1, k);
        // Multiply by (n - k) / k
        result = (result * (n - k)) / k;
    }
    if (result == Long.MAX_VALUE) {
        throw new ArithmeticException("result too large to represent in a long integer");
    }
    return result;
}

## Fixed Function 2
public static double binomialCoefficientDouble(final int n, final int k) {
    if (n < k) {
        throw new IllegalArgumentException("must have n >= k for binomial coefficient (n,k)");
    }
    if (n < 0) {
        throw new IllegalArgumentException("must have n >= 0 for binomial coefficient (n,k)");
    }
    if ((n == k) || (k == 0)) {
        return 1.0;
    }
    if ((k == 1) || (k == n - 1)) {
        return (double) n;
    }
    double result = 1;
    if (k > n - k) {
        // Use symmetry for large k
        // We use the formula
        // (n choose k) = n! / (n-k)! / k!
        // (n choose k) == ((n-k+1)*...*n) / (1*...*k)
        // which could be written
        // (n choose k) == (n-1 choose k-1) * n / k
        // Calculate (n-1 choose k-1) instead of (n choose k)
        result = binomialCoefficientDouble(n - 1, k - 1);
        // Multiply by n / k
        result *= ((double) n) / k;
    } else {
        // Calculate (n-1 choose k) instead of (n choose k)
        result = binomialCoefficientDouble(n - 1, k);
        // Multiply by (n - k) / k
        result *= ((double) (n - k)) / k;
    }
    if (result == Double.POSITIVE_INFINITY) {
        throw new ArithmeticException("result too large to represent in a double");
    }
    return result;
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