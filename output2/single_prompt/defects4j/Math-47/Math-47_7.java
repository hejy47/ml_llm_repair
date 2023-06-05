## Fixed Function 1
public Complex(double real, double imaginary) {
    this.real = real;
    this.imaginary = imaginary;
    this.isNaN = Double.isNaN(real) || Double.isNaN(imaginary);
    this.isInfinite = !this.isNaN && (Double.isInfinite(real) || Double.isInfinite(imaginary));
}

## Fixed Function 2
public Complex divide(Complex divisor) throws NullArgumentException {
    MathUtils.checkNotNull(divisor);
    if (this.isNaN || divisor.isNaN) {
        return Complex.NaN;
    }
    if (divisor.getReal() == 0.0 && divisor.getImaginary() == 0.0) {
        return Complex.NaN;
    }
    if (divisor.isInfinite() && !this.isInfinite()) {
        return Complex.ZERO;
    }
    final double c = divisor.getReal();
    final double d = divisor.getImaginary();
    if (FastMath.abs(c) < FastMath.abs(d)) {
        double q = c / d;
        double denominator = c * q + d;
        return createComplex((this.real * q + this.imaginary) / denominator, (this.imaginary * q - this.real) / denominator);
    } else {
        double q = d / c;
        double denominator = d * q + c;
        return createComplex((this.imaginary * q + this.real) / denominator, (this.imaginary - this.real * q) / denominator);
    }
}

## Fixed Function 3
public Complex divide(double divisor) {
    if (this.isNaN || Double.isNaN(divisor)) {
        return Complex.NaN;
    }
    if (divisor == 0.0d) {
        return Complex.NaN;
    }
    if (Double.isInfinite(divisor)) {
        return !this.isInfinite() ? Complex.ZERO : Complex.NaN;
    }
    return createComplex(this.real / divisor, this.imaginary / divisor);
}