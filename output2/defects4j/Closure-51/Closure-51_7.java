## Fixed Function 1
void addNumber(double x) {
    // This is not pretty printing. This is to prevent misparsing of x- -4 as
    // x--4 (which is a syntax error).
    char prev = getLastChar();
    if (x < 0 && prev == '-') {
        add(" ");
    }
    // Use Double.compare to compare double values for equality
    if (Double.compare((long)x, x) == 0) {
        long value = (long) x;
        long mantissa = value;
        int exp = 0;
        if (Math.abs(x) >= 100) {
            // Use Math.pow instead of ** for exponentiation
            while (mantissa / 10 * Math.pow(10, exp + 1) == value) {
                mantissa /= 10;
                exp++;
            }
        }
        // Use String.format to format the mantissa and exponent
        if (exp > 2) {
            add(String.format("%dE%d", mantissa, exp));
        } else {
            add(String.format("%d", value));
        }
    } else {
        add(String.valueOf(x));
    }
}