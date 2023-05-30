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
    if (k > (n/2)) {
        return binomialCoefficient(n, n-k);
    }
    
    for (int i=0; i<k; i++) {
        result *= (n-i);
        result /= (i+1);
        if (result >= Long.MAX_VALUE/(n-i)) {
            throw new ArithmeticException("result too large to represent in a long integer");
        }
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

    // avoid infinite loop
    if (k>n-k) {
        return binomialCoefficientLog(n, n-k);
    }

    for (int i = k + 1; i <= n; i++) {
        logSum += Math.log((double) i);
    }
    for (int i = 2; i <= n - k; i++) {
        logSum -= Math.log((double) i);
    }
    return logSum;
}