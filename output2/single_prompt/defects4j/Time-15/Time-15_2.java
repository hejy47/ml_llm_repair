## Fixed Function 1
public static long safeMultiply(long val1, int val2) {
    if(val2 == -1) {
        return -val1;
    } else if(val2 == 0) {
        return 0L;
    } else if(val2 == 1) {
        return val1;
    } else {
        long total = val1 * val2;
        if (total / val2 != val1) {
            throw new ArithmeticException("Multiplication overflows a long: " + val1 + " * " + val2);
        }
        return total;
    }
}