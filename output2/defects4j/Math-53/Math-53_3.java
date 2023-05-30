## Fixed Function 1
public Complex add(Complex rhs) throws NullArgumentException {
    MathUtils.checkNotNull(rhs);
    double realResult = real + rhs.getReal();
    double imaginaryResult = imaginary + rhs.getImaginary();
    return createComplex(realResult, imaginaryResult);
}