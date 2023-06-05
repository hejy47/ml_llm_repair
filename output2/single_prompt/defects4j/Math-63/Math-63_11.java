## Fixed Function 1
public static boolean equals(double x, double y) {
    return (Math.abs(x - y) < 0.000001);
}
## Fixed Function 2
public static boolean equals(double x, double y) {
    return Double.compare(x, y) == 0;
}