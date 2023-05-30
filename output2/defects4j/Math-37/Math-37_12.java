## Fixed Function 1
public Complex tan() {
    if (isNaN) {
        return NaN;
    }
    double real2 = 2.0 * real;
    double imaginary2 = 2.0 * imaginary;
    double d = FastMath.cos(real2) + FastMath.cosh(imaginary2);
    return createComplex(FastMath.sin(real2) / d, FastMath.sinh(imaginary2) / d).divide(createComplex(1, 0).cos(real)); // added division by cosine of real
}

## Fixed Function 2
public Complex tanh() {
    if (isNaN) {
        return NaN;
    }
    double real2 = 2.0 * real;
    double imaginary2 = 2.0 * imaginary;
    double d = FastMath.cosh(real2) + FastMath.cos(imaginary2);
    return createComplex(FastMath.sinh(real2) / d, FastMath.sin(imaginary2) / d).divide(createComplex(0, 1).cos(imaginary)); // added division by cosine of imaginary
}