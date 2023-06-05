## Fixed Function 1
public static long safeMultiply(long val1, int val2) {
    switch(val2) {
        case -1:
            return -val1;
        case 0:
            return 0L;
        case 1:
            return val1;
        case Integer.MIN_VALUE:
            if(val1 == -1) {
                return Integer.MIN_VALUE;
            } else {
                throw new ArithmeticException("Multiplication overflows a long: " + val1 + " * " + val2);
            }
        default:
            long total = val1 * val2;
            if (total / val2 != val1) {
                throw new ArithmeticException("Multiplication overflows a long: " + val1 + " * " + val2);
            }
            return total;
    }
}