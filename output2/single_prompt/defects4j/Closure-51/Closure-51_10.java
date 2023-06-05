## Fixed Function 1
void addNumber(double x) {
    // This is not pretty printing. This is to prevent misparsing of x- -4 as
    // x--4 (which is a syntax error).
    char prev = getLastChar();
    if (x < 0 && prev == '-') {
        add(" ");
    }
    if ((long) x == x) {
        long value = (long) x;
        long mantissa = Math.abs(value);
        int exp = 0;
        if (Math.abs(x) >= 100) {
            while (mantissa / 10 * Math.pow(10, exp + 1) == Math.abs(value)) {
                mantissa /= 10;
                exp++;
            }
        }
        if (exp > 2) {
            add((value < 0 ? "-" : "") + String.format("%dE%d", mantissa, exp));
        } else {
            add(Long.toString(value));
        }
    } else {
        add(String.valueOf(x));
    }
}