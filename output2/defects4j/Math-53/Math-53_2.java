## Fixed Function 1
public Complex add(Complex rhs) throws NullArgumentException {
    MathUtils.checkNotNull(rhs);
    double newReal = real + rhs.getReal();
    double newImaginary = imaginary + rhs.getImaginary();
    return createComplex(newReal, newImaginary);
}