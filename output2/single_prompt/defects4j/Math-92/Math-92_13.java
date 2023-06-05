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
    // Use symmetry for large k
    if (k > n / 2) {
        return binomialCoefficient(n, n - k);
    }
    long result = 1;
    for (int i = 0; i < k; i++) {
        result *= (n - i);
        result /= (i + 1);
    }
    return result;
}

## Fixed Function 2
public static double binomialCoefficientDouble(final int n, final int k) {
    return Math.exp(binomialCoefficientLog(n, k));
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
    // (n choose k) = n! / (n-k)! / k!
    // (n choose k) == ((n-k+1)*...*n) / (1*...*k)
    // which could be written
    // (n choose k) == (n-1 choose k-1) * n / k
    // For large k, use the symmetry relation:
    if (k > n / 2) {
        return binomialCoefficientLog(n, n - k);
    }
    // Perform the multiplication avoiding unnecessary overflow/underflow.
    for (int i = 1; i <= k; i++) {
        logSum += Math.log((double) (n - i + 1)) - Math.log((double) i);
    }
    return logSum;
}