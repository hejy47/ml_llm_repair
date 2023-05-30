## Fixed Function 1
public Complex tan() {
    if (isNaN) {
        return NaN;
    }
    double real2 = 2.0 * real;
    double imaginary2 = 2.0 * imaginary;
    double sinReal2 = FastMath.sin(real2);
    double cosReal2 = FastMath.cos(real2);
    double coshImaginary2 = FastMath.cosh(imaginary2);
    double d = cosReal2 + coshImaginary2;
    return createComplex(sinReal2 / d, FastMath.sinh(imaginary2) / d);
}

## Fixed Function 2
public Complex tanh() {
    if (isNaN) {
        return NaN;
    }
    double real2 = 2.0 * real;
    double imaginary2 = 2.0 * imaginary;
    double sinhReal2 = FastMath.sinh(real2);
    double cosImaginary2 = FastMath.cos(imaginary2);
    double coshReal2 = FastMath.cosh(real2);
    double d = coshReal2 + cosImaginary2;
    return createComplex(sinhReal2 / d, FastMath.sin(imaginary2) / d);
}