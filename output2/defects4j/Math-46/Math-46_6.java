## Fixed Function 1
public Complex divide(Complex divisor) throws NullArgumentException {
    MathUtils.checkNotNull(divisor);
    if (isNaN || divisor.isNaN) {
        return NaN;
    }
    if (divisor.isZero) {
        return isZero ? NaN : INF;
    }
    if (divisor.isInfinite() && !isInfinite()) {
        return ZERO;
    }
    final double c = divisor.getReal();
    final double d = divisor.getImaginary();
    if (FastMath.abs(c) < FastMath.abs(d) && !doublyEquals(d, 0.0)) {
        double q = c / d;
        double denominator = c * q + d;
        return createComplex((real * q + imaginary) / denominator, (imaginary * q - real) / denominator);
    } else {
        double q = d / c;
        double denominator = d * q + c;
        return createComplex((imaginary * q + real) / denominator, (imaginary - real * q) / denominator);
    }
}

private boolean doublyEquals(double a, double b) {
    return a == b || Double.doubleToLongBits(a) == Double.doubleToLongBits(b);
}

## Fixed Function 2
public Complex divide(double divisor) {
    if (isNaN || Double.isNaN(divisor)) {
        return NaN;
    }
    if (doublyEquals(divisor, 0.0)) {
        return isZero ? NaN : INF;
    }
    if (Double.isInfinite(divisor)) {
        return !isInfinite() ? ZERO : NaN;
    }
    return createComplex(real / divisor, imaginary / divisor);
}

private boolean doublyEquals(double a, double b) {
    return a == b || Double.doubleToLongBits(a) == Double.doubleToLongBits(b);
}