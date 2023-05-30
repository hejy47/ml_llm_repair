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
    // We use the formula
    // (n choose k) = n! / (n-k)! / k!
    // (n choose k) == ((n-k+1)*...*n) / (1*...*k)
    // which could be written
    // (n choose k) == (n-1 choose k-1) * n / k
    if (n <= 61) {
        // For n <= 61, the naive implementation cannot overflow.
        long result = 1;
        for (int i = 1; i <= k; i++) {
            result *= (n - k + i);
            result /= i;
        }
        return result;
    } else if (n <= 66) {
        // For n > 61 but n <= 66, the result cannot overflow,
        // but we must take care not to overflow intermediate values.
        // We know that (result * i) is divisible by j,
        // but (result * i) may overflow, so we split j:
        // Filter out the gcd, d, so j/d and i/d are integer.
        // result is divisible by (j/d) because (j/d)
        // is relative prime to (i/d) and is a divisor of
        // result * (i/d).
        long result = 1;
        for (int i = 1; i <= k; i++) {
            long j = n - k + i;
            long d = gcd(result, i);
            result /= d;
            j /= d;
            result *= j;
        }
        return result;
    } else {
        // For n > 66, a result overflow might occur, so we check
        // the multiplication, taking care to not overflow
        // unnecessarily.
        long result = 1;
        for (int i = 1; i <= k; i++) {
            long j = n - k + i;
            long d1 = gcd(result, i);
            long d2 = gcd(j, k);
            result /= d1;
            j /= d2;
            result *= j;
        }
        if (result < 0) {
            throw new ArithmeticException("result too large to represent in a long integer");
        }
        return result;
    }
}

private static long gcd(long a, long b) {
    while (b != 0) {
        long temp = b;
        b = a % b;
        a = temp;
    }
    return a;
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