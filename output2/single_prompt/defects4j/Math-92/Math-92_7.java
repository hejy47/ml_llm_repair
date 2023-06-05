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
    if (n <= 61) {
        // naive implementation for small n
        long result = 1;
        for (int i = 0; i < k; i++) {
            result *= (n - i);
            result /= (i + 1);
        }
        return result;
    } else {
        // use symmetry for large k
        if (k > n - k) {
            return binomialCoefficient(n, n - k);
        }
        // multiply first k terms of (n-k+1)...n/(1...k)
        long result = 1;
        for (int i = 1; i <= k; i++) {
            result *= (n - k + i);
            result /= i;
        }
        return result;
    }
}

## Fixed Function 2
public static double binomialCoefficientDouble(final int n, final int k) {
    return Math.floor(Math.exp(binomialCoefficientLog(n, k)));
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
    if ((k == 1) || (k == n - 1))
        return Math.log((double) n);
    double logSum = 0;
    // use symmetry for large k
    if (k > n - k) {
        return binomialCoefficientLog(n, n - k);
    }
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