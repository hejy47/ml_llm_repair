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
            // using Math.multiplyExact to perform multiplication and throw exception if overflow occurs
            return Math.multiplyExact(val1, val2);
    }
}