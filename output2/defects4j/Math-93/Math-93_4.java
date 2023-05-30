## Fixed Function 1
public static long factorial(final int n) {
    if (n < 0) {
        throw new IllegalArgumentException("must have n >= 0 for n!");
    }
    if (n <= 1) {
        return 1;
    }
    long result = 1;
    for (int i = 2; i <= n; i++) {
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
    if (n == 0) {
        return 1.0;
    }
    double result = 1.0;
    for (int i = 2; i <= n; i++) {
        result *= i;
        if (result == Double.POSITIVE_INFINITY || result == Double.NEGATIVE_INFINITY) {
            throw new ArithmeticException("factorial value is too large to fit in a double");
        }
    }
    return result;
}

## Fixed Function 3
public static double factorialLog(final int n) {
    if (n <= 0) {
        throw new IllegalArgumentException("must have n > 0 for n!");
    }
    double logSum = 0;
    for (int i = 2; i <= n; i++) {
        logSum += Math.log(i);
    }
    return logSum;
}