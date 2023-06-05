## Fixed Function 1
public static boolean equals(double x, double y) {
    return Math.abs(x - y) < 1e-9 || (Double.isNaN(x) && Double.isNaN(y));
    //using Math.abs() with small epsilon instead of exact comparison ensures that small differences will not be considered as unequal
}