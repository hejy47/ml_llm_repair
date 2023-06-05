## Fixed Function 1
public static long factorial(final int n) {
    if(n < 0) {
        throw new IllegalArgumentException("must have n >= 0 for n!");
    }
    if(n > 20) {
        throw new IllegalArgumentException("must have n <= 20 since 21! > Long.MAX_VALUE");
    }
    long result = 1;
    for(int i = 2; i <= n; i++) {
        result *= i;
    }
    return result;
}

## Fixed Function 2
public static double factorialDouble(final int n) {
    if(n < 0) {
        throw new IllegalArgumentException("must have n >= 0 for n!");
    }
    double result = 1;
    for(int i = 2; i <= n; i++) {
        result *= i;
    }
    return result;
}

## Fixed Function 3
public static double factorialLog(final int n) {
    if(n < 0) {
        throw new IllegalArgumentException("must have n >= 0 for n!");
    }
    if(n == 0) {
        return 0;
    }
    double logSum = 0;
    for(int i = 1; i <= n; i++) {
        logSum += Math.log(i);
    }
    return logSum;
}