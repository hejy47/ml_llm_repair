## Fixed Function 1
public static long factorial(final int n) {
    long result = 1;
    for (int i = 1; i <= n; i++) {
        result *= i;
        if (result < 0) {
            throw new ArithmeticException("factorial value is too large to fit in a long");
        }
    }
    return result;
}

## Fixed Function 2
public static double factorialDouble(final int n) {
    if (n < 0) {
        throw new IllegalArgumentException("must have n >= 0 for n!");
    }
    double result = 1;
    for (int i = 1; i <= n; i++) {
        result *= i;
    }
    return result;
}

## Fixed Function 3
public static double factorialLog(final int n) {
    if (n <= 0) { // changed condition to n <= 0 to handle cases when n = 0
        throw new IllegalArgumentException("must have n >= 0 for n!");
    }
    double logSum = 0;
    for (int i = 1; i <= n; i++) { // changed loop start condition to i = 1 to handle cases when n = 1
        logSum += Math.log((double) i);
    }
    return logSum;
}