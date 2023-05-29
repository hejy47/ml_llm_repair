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
        long mantissa = value;
        int exp = 0;
        double absX = Math.abs(x);
        if (absX >= 100) {
            while (mantissa / (long) Math.pow(10, exp + 1) == value / (long) Math.pow(10, exp + 1)) {
                mantissa /= 10;
                exp++;
            }
        }
        if (exp > 2) {
            add(Long.toString(mantissa) + "E" + Integer.toString(exp));
        } else {
            add(Long.toString(value));
        }
    } else {
        add(String.valueOf(x));
    }
}