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
    if (FastMath.abs(c) < FastMath.abs(d)) {
        double q = c / d;
        double denominator = c * q + d;
        return createComplex((real * q + imaginary) / denominator, (imaginary * q - real) / denominator);
    } else if (c != 0.0 && d != 0.0) { // Avoid division by zero
        double q = d / c;
        double denominator = d * q + c;
        return createComplex((imaginary * q + real) / denominator, (imaginary - real * q) / denominator);
    } else if (c == 0.0 && d != 0.0) {
        double denominator = d;
        return createComplex(imaginary / denominator, -real / denominator);
    } else if (c != 0.0 && d == 0.0) {
        double denominator = c;
        return createComplex(real / denominator, imaginary / denominator);
    } else { // c == 0.0 && d == 0.0, Should not enter this code
        return NaN;
    }
}

## Fixed Function 2
public Complex divide(double divisor) {
    if (isNaN || Double.isNaN(divisor)) {
        return NaN;
    }
    if (divisor == 0d) {
        return isZero ? NaN : INF;
    }
    if (Double.isInfinite(divisor)) {
        return !isInfinite() ? ZERO : NaN;
    }
    return createComplex(real / divisor, imaginary / divisor);
}