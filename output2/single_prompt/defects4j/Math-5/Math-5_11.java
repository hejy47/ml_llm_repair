## Fixed Function 1
public Complex reciprocal() {
    if (isNaN) {
        return NaN;
    }
    if (real == 0.0 && imaginary == 0.0) {
        return NaN;
    }
    if (isInfinite) {
        return ZERO;
    }
    double scale = 1.0 / (real * real + imaginary * imaginary);
    return createComplex(real * scale, -imaginary * scale);
}