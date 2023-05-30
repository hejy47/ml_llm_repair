## Fixed Function 1
public Complex add(Complex rhs) throws NullArgumentException {
    MathUtils.checkNotNull(rhs, "Null argument passed to add method");
    return createComplex(real + rhs.getReal(), imaginary + rhs.getImaginary());
}