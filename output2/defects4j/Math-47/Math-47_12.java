## Fixed Function 1
public Complex(double real, double imaginary) {
    this.real = real;
    this.imaginary = imaginary;
    // Corrected the typo, changed '||' to '&&'
    isNaN = Double.isNaN(real) && Double.isNaN(imaginary);
    isInfinite = !isNaN && (Double.isInfinite(real) || Double.isInfinite(imaginary));
}

## Fixed Function 2
public Complex divide(Complex divisor) throws NullArgumentException {
    MathUtils.checkNotNull(divisor);
    if (isNaN || divisor.isNaN) {
        return NaN;
    }
    if (divisor.getReal() == 0.0 && divisor.getImaginary() == 0.0) {
        return NaN;
    }
    if (!isInfinite() && divisor.isInfinite()) {
        return ZERO;
    }
    final double c = divisor.getReal();
    final double d = divisor.getImaginary();
    if (FastMath.abs(c) < FastMath.abs(d)) {
        double q = c / d;
        double denominator = c * q + d;
        return createComplex((real * q + imaginary) / denominator, (imaginary * q - real) / denominator);
    } else {
        double q = d / c;
        double denominator = d * q + c;
        return createComplex((imaginary * q + real) / denominator, (imaginary - real * q) / denominator);
    }
}

## Fixed Function 3
public Complex divide(double divisor) {
    if (isNaN || Double.isNaN(divisor)) {
        return NaN;
    }
    if (divisor == 0d) {
        return NaN;
    }
    if (Double.isInfinite(divisor)) {
        return isInfinite() ? NaN : ZERO;
    }
    return createComplex(real / divisor, imaginary / divisor);
}