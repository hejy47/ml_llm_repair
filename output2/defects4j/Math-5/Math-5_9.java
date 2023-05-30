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
    if (FastMath.abs(real) < FastMath.abs(imaginary)) {
        double q = real / imaginary;
        double scale = 1. / (real * q + imaginary * q);
        return createComplex(scale * q, -scale);
    } else {
        double q = imaginary / real;
        double scale = 1. / (imaginary * q + real * q);
        return createComplex(scale, -scale * q);
    }
}