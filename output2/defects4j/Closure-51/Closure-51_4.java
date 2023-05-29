## Fixed Function 1
void addNumber(double x) {
    char prev = getLastChar();
    if (x < 0 && prev == '-') {
        add(" ");
    }
    if ((long) x == x) {
        long value = (long) x;
        if (Math.abs(x) >= 10E6 || Math.abs(x) < 10E-6) { // add condition to handle scientific notation
            add(String.format("%1.2E", x)); // format output as scientific notation with 2 decimal places
        } else {
            add(Long.toString(value));
        }
    } else {
        add(String.valueOf(x));
    }
}