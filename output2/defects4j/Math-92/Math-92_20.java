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
        for (int i = n - k + 1; i <= n; i++) {
            result *= i;
        }
        for (int i = 2; i <= k; i++) {
            result /= i;
        }
    } else {
        for (int i = k + 1; i <= n; i++) {
            result *= i;
        }
        for (int i = 2; i <= n - k; i++) {
            result /= i;
        }
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
    double logSum = 0;
    if (k > n - k) {
        for (int i = n - k + 1; i <= n; i++) {
            logSum += Math.log((double) i);
        }
        for (int i = 2; i <= k; i++) {
            logSum -= Math.log((double) i);
        }
    } else {
        for (int i = k + 1; i <= n; i++) {
            logSum += Math.log((double) i);
        }
        for (int i = 2; i <= n - k; i++) {
            logSum -= Math.log((double) i);
        }
    }
    return Math.exp(logSum);
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
    if (k > n - k) {
        for (int i = n - k + 1; i <= n; i++) {
            logSum += Math.log((double) i);
        }
        for (int i = 2; i <= k; i++) {
            logSum -= Math.log((double) i);
        }
    } else {
        for (int i = k + 1; i <= n; i++) {
            logSum += Math.log((double) i);
        }
        for (int i = 2; i <= n - k; i++) {
            logSum -= Math.log((double) i);
        }
    }
    return logSum;
}