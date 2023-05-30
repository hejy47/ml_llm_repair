## Fixed Function 1
public Complex(double real, double imaginary) {
    this.real = real;
    this.imaginary = imaginary;
    isNaN = Double.isNaN(real) || Double.isNaN(imaginary);
    isInfinite = !isNaN && (Double.isInfinite(real) || Double.isInfinite(imaginary));
    // Fix NaN and infinite values
    if (Double.isNaN(real)) {
        this.real = 0.0;
        this.imaginary = 0.0;
        isNaN = true;
        isInfinite = false;
    } else if (Double.isInfinite(real) && !Double.isNaN(imaginary)) {
        this.real = Double.POSITIVE_INFINITY;
        this.imaginary = 0.0;
        isNaN = false;
        isInfinite = true;
    } else if (Double.isInfinite(imaginary) && !Double.isNaN(real)) {
        this.real = 0.0;
        this.imaginary = Double.POSITIVE_INFINITY;
        isNaN = false;
        isInfinite = true;
    } else if (Double.isInfinite(real) && Double.isInfinite(imaginary)) {
        this.real = Double.POSITIVE_INFINITY;
        this.imaginary = Double.POSITIVE_INFINITY;
        isNaN = false;
        isInfinite = true;
    }
}

## Fixed Function 2
public Complex divide(Complex divisor) throws NullArgumentException {
    MathUtils.checkNotNull(divisor);
    if (isNaN || divisor.isNaN) {
        return NaN;
    }
    if (divisor.real == 0.0 && divisor.imaginary == 0.0) {
        return NaN;
    }
    if (divisor.isInfinite() && !isInfinite()) {
        return ZERO;
    }
    final double c = divisor.real;
    final double d = divisor.imaginary;
    // Fix division by zero error
    if (c == 0.0 && d == 0.0) {
        return NaN;
    }
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
    // Fix division by zero error
    if (divisor == 0d) {
        return NaN;
    }
    if (Double.isInfinite(divisor)) {
        return !isInfinite() ? ZERO : NaN;
    }
    return createComplex(real / divisor, imaginary / divisor);
}