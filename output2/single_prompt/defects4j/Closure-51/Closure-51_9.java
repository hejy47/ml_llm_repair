## Fixed Function 1
void addNumber(double x) {
    char prev = getLastChar();
    if (x < 0 && prev == '-') {
        add(" ");
    }
    if ((long) x == x) {
        long value = (long) x;
        if (value > 0) {
            add(Long.toString(value));
        } else {
            add("(" + Long.toString(value) + ")");
        }
    } else {
        add(String.valueOf(x));
    }
}