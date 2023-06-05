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
        // Use naive implementation for small n
        // to avoid overflow issues
        long result = n;
        for (int i = 2; i <= k; i++) {
            result *= (n - i + 1);
            result /= i;
        }
        return result;
    } else if (n <= 66) {
        // Use optimized implementation for n > 61
        // but n <= 66 to avoid intermediate overflow
        long result = n;
        for (int i = 2; i <= k; i++) {
            result *= (n - i + 1);
            result /= i;
            long gcd = MathUtil.gcd(result, i);
            result /= gcd;
            i /= gcd;
        }
        return result;
    } else {
        // Use standard implementation for large n
        // to avoid overflow issues
        long result = Math.round(binomialCoefficientDouble(n, k));
        if (result == Long.MAX_VALUE) {
            throw new ArithmeticException("result too large to represent in a long integer");
        }
        return result;
    }
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
        return 1;
    }
    if ((k == 1) || (k == n - 1)) {
        return (double) n;
    }
    if (n <= 66) {
        // Use optimized implementation for small n
        // to avoid intermediate overflow
        double result = (double) n;
        for (int i = 2; i <= k; i++) {
            result *= (double) (n - i + 1);
            result /= (double) i;
        }
        return result;
    } else {
        // Use logarithmic implementation for large n
        // to avoid overflow issues
        double logResult = binomialCoefficientLog(n, k);
        return Math.exp(logResult);
    }
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