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
    if (val1 == Long.MIN_VALUE && val2 == -1) {
        throw new ArithmeticException("Multiplication overflows a long");
    }
    long total = val1 * val2;
    if (total / val2 != val1) {
        throw new ArithmeticException("Multiplication overflows a long: " + val1 + " * " + val2);
    }
    return total;
}

The fixed function added a check for the specific case where the first operand is Long.MIN_VALUE and the second operand is -1, which overflows a long.