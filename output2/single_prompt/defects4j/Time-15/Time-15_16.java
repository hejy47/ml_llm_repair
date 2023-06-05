## Fixed Function 1
public static long safeMultiply(long val1, int val2) {
    switch(val2) {
        case -1:
            return -val1;
        case 0:
            return 0L;
        case 1:
            return val1;
    }
    if (val2 > 0 ? val1 > Long.MAX_VALUE / val2 || val1 < Long.MIN_VALUE / val2
                   : (val2 < -1 ? val1 > Long.MIN_VALUE / val2 || val1 < Long.MAX_VALUE / val2
                                 : val2 == Integer.MIN_VALUE && val1 != -1))
        throw new ArithmeticException("Multiplication overflows a long: " + val1 + " * " + val2);
    return val1 * val2;
}