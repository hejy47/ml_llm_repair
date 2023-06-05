## Fixed Function 1
public static double cosh(double x) {
    if (Double.isNaN(x)) {
        return Double.NaN;
    }
    if (x > 20) {
        // Avoid overflow (MATH-905).
        return Math.exp(x) / 2.0;
    }
    if (x < -20) {
        // Avoid overflow (MATH-905).
        return Math.exp(-x) / 2.0;
    }
    double expX = Math.exp(x);
    double expNegX = Math.exp(-x);
    return (expX + expNegX) / 2.0;
}

## Fixed Function 2
public static double sinh(double x) {
    if (Double.isNaN(x)) {
        return Double.NaN;
    }
    if (x > 20) {
        // Avoid overflow (MATH-905).
        return Math.exp(x) / 2.0;
    }
    if (x < -20) {
        // Avoid overflow (MATH-905).
        return -Math.exp(-x) / 2.0;
    }
    if (x == 0.0) {
        return x;
    }
    double expX = Math.exp(x);
    double expNegX = Math.exp(-x);
    double sinhX = (expX - expNegX) / 2.0;
    if (Double.isInfinite(sinhX)) {
        return expX / 2.0;
    }
    return sinhX;
}