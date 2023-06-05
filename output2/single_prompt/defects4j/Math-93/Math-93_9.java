## Fixed Function 1
public static long factorial(final int n) {
    if (n < 0) {
        throw new IllegalArgumentException("must have n >= 0 for n!");
    }
    if (n < factorials.length && factorials[n] != 0) {
        return factorials[n];
    }
    long result = 1;
    for (int i = 2; i <= n; i++) {
        result *= i;
        if (i < factorials.length) {
            factorials[i] = result;
        }
    }
    if (result == Long.MAX_VALUE) {
        throw new ArithmeticException("factorial value is too large to fit in a long");
    }
    return result;
}

## Fixed Function 2
public static double factorialDouble(final int n) {
    if (n < 0) {
        throw new IllegalArgumentException("must have n >= 0 for n!");
    }
    return Math.exp(factorialLog(n));
}

## Fixed Function 3
public static double factorialLog(final int n) {
    if (n <= 1) {
        return 0;
    }
    double logSum = 0;
    for (int i = 2; i <= n; i++) {
        logSum += Math.log((double) i);
    }
    return logSum;
}