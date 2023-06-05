## Fixed Function 1
void addNumber(double x) {
    // This is not pretty printing. This is to prevent misparsing of x- -4 as
    // x--4 (which is a syntax error).
    char prev = getLastChar();
    boolean negativeZero = isNegativeZero(x);
    if (x < 0 && prev == '-') {
        add(" ");
    }
    if ((long) x == x && !negativeZero) {
        long value = (long) x;
        long mantissa = value;
        int exp = 0;
        if (Math.abs(x) >= 100) {
            while (mantissa % 10 == 0) {
                mantissa /= 10;
                exp++;
            }
        }
        if (exp > 2 || exp < -2) {
            add(String.format("%.0E", x));
        } else {
            add(Long.toString(value));
        }
    } else {
        add(String.format("%.16g", x));
    }
}