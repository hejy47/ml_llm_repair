## Fixed Function 1
public Complex reciprocal() {
    if (isNaN) {
        return NaN;
    }
    if (real == 0.0 && imaginary == 0.0) {
        // Return NaN instead of throwing an exception
        return NaN;
    }
    if (isInfinite) {
        // Return NaN instead of throwing an exception
        return NaN;
    }
    if (FastMath.abs(real) < FastMath.abs(imaginary)) {
        double q = real / imaginary;
        double scale = 1. / (real * q + imaginary);
        return createComplex(scale * q, -scale);
    } else {
        double q = imaginary / real;
        double scale = 1. / (imaginary * q + real);
        return createComplex(scale, -scale * q);
    }
}