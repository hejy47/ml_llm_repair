## Fixed Function 1
public Complex tan() {
    if (isNaN) {
        return NaN;
    }
    double real2 = 2.0 * real;
    double imaginary2 = 2.0 * imaginary;
    double d = FastMath.cos(real2) + FastMath.cosh(imaginary2);
    if (d == 0) {
        return NaN;
    }
    return createComplex(FastMath.sin(real2) / d, FastMath.sinh(imaginary2) / d);
}

## Fixed Function 2
public Complex tanh() {
    if (isNaN) {
        return NaN;
    }
    double real2 = 2.0 * real;
    double imaginary2 = 2.0 * imaginary;
    double d = FastMath.cosh(real2) + FastMath.cos(imaginary2);
    if (d == 0) {
        return NaN;
    }
    return createComplex(FastMath.sinh(real2) / d, FastMath.sin(imaginary2) / d);
}