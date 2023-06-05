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
    if ((val1 > 0 && val2 > Long.MAX_VALUE / val1) ||
        (val1 < 0 && val2 < Long.MIN_VALUE / val1)) {
        throw new ArithmeticException("Multiplication overflows a long: " + val1 + " * " + val2);
    }
    return val1 * val2;
} 

The fixed function uses a different approach to check for overflow during multiplication. Instead of performing the multiplication and checking if it overflows afterwards, it checks if multiplying the absolute values of the operands would exceed the range of a long before performing the multiplication. This approach allows for catching overflow cases without actually performing the multiplication.