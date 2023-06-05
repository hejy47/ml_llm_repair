## Fixed Function 1
public static boolean equals(double x, double y) {
    return (Double.doubleToLongBits(x) == Double.doubleToLongBits(y)) || (Double.isNaN(x) && Double.isNaN(y));
}