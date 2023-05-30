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
    if (n <= 61) { // handle n <= 61
        long result = 1;
        for (int i = 0; i < k; i++) {
            result *= (n - i);
            result /= (i + 1);
        }
        return result;
    } else if (n <= 66) { // handle 61 < n <= 66
        long result = 1;
        int i = 0;
        for (; i < k / 2; i++) {
            result *= (n - i);
            result /= (i + 1);
        }
        for (; i < k; i++) {
            result *= (n - i);
        }
        return result;
    } else { // handle n > 66
        int largestIndex = -1;
        long maxValue = Long.MAX_VALUE / n;
        long result = 1;
        for (int i = 0; i < k; i++) {
            if (i <= k - (n - k)) {
                result *= (n - i);
                result /= (i + 1);
            } else {
                if (result > maxValue) {
                    largestIndex = i - 1;
                    break;
                }
                result *= (n - i);
                result /= (i + 1);
            }
        }
        if (largestIndex == -1) {
            return result;
        }
        double logSum = binomialCoefficientLog(n, k);
        for (int i = 0; i <= largestIndex; i++) {
            logSum += Math.log((double) (n - i) / (double) (i + 1));
        }
        return Math.round(Math.exp(logSum));
    }
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