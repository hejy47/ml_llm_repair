## Fixed Function 1
public static long safeMultiply(long val1, int val2) {
    switch(val2) {
        case -1:
            return -val1;
        case 0:
            return 0L;
        case 1:
            return val1;
        default:
            if (val1 == 0) {
                return 0L;
            }
            long total = val1 * val2;
            if (total / val1 != val2) {
                throw new ArithmeticException("Multiplication overflows a long: " + val1 + " * " + val2);
            }
            return total;
    }
} 

The fixed function checks if `val1` is equal to 0 before calculating `total`. Additionally, it uses `total / val1` to check for overflow instead of `total / val2` to ensure that both values are accounted for in the calculation.