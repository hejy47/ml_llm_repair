## Fixed Function 1
public Complex(double real, double imaginary) {
    this.real = real;
    this.imaginary = imaginary;
    isNaN = Double.isNaN(real) || Double.isNaN(imaginary);
    isInfinite = !isNaN && (Double.isInfinite(real) || Double.isInfinite(imaginary));
    // added check for negative zero
    if (real == 0 && Double.doubleToLongBits(real) == Double.doubleToLongBits(-0.0)) {
        this.real = 0.0;
    }
    if (imaginary == 0 && Double.doubleToLongBits(imaginary) == Double.doubleToLongBits(-0.0)) {
        this.imaginary = 0.0;
    }
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
    if (divisor.isInfinite() && !isInfinite()) {
        return ZERO;
    }
    final double c = divisor.getReal();
    final double d = divisor.getImaginary();
    if (FastMath.abs(c) < FastMath.abs(d)) {
        double q = c / d;
        double denominator = c * q + d;
        return createComplex((real * q + imaginary * q) / denominator, (imaginary * q - real) / denominator);
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
        return !isInfinite() ? ZERO : NaN;
    }
    // added check for negative zero
    if (divisor == 0 && Double.doubleToLongBits(divisor) == Double.doubleToLongBits(-0.0)) {
        return createComplex(real / -0.0, imaginary / -0.0);
    }
    return createComplex(real / divisor, imaginary / divisor);
}