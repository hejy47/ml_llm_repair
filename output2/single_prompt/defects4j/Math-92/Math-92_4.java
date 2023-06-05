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
    // Avoid multiplication overflow by using a separate numerator
    for (int i = 1; i <= k; ++i) {
        result *= n - k + i;
        result /= i;
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
    // Avoid multiplication overflow by using logarithmic sums
    for (int i = 1; i <= k; ++i) {
        logSum += Math.log(n - k + i) - Math.log(i);
    }
    return logSum;
}