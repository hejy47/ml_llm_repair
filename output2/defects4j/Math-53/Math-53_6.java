## Fixed Function 1
public Complex add(Complex rhs) throws NullArgumentException {
    MathUtils.checkNotNull(rhs, "Complex number is null");
    return createComplex(real + rhs.getReal(), imaginary + rhs.getImaginary());
}