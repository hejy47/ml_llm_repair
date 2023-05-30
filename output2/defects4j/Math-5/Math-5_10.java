## Fixed Function 1
public Complex reciprocal() {
    if (isNaN() || isInfinite()) {
        return NaN;
    }
    if (real == 0.0 && imaginary == 0.0) {
        return NaN;
    }
    double scale = 1. / (real * real + imaginary * imaginary);
    return createComplex(real * scale, -imaginary * scale);
}