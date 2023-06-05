## Fixed Function 1
public Complex reciprocal() {
    if (isNaN || (real == 0.0 && imaginary == 0.0)) {
        return Complex.NaN;
    }
    if (isInfinite) {
        return Complex.ZERO;
    }
    double scale = 1.0 / (real * real + imaginary * imaginary);
    return createComplex(real * scale, -imaginary * scale);
}