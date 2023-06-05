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
    double scale;
    double absReal = FastMath.abs(real);
    double absImag = FastMath.abs(imaginary);
    if (absReal >= absImag) {
        double q = imaginary / real;
        scale = 1.0 / (real + q * imaginary);
        return createComplex(scale, -scale * q);
    } else {
        double q = real / imaginary;
        scale = 1.0 / (q * real + imaginary);
        return createComplex(scale * q, -scale);
    }
}