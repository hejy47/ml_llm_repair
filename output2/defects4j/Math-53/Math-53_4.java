## Fixed Function 1
public Complex add(Complex rhs) throws NullArgumentException {
    MathUtils.checkNotNull(rhs);
    double realPart = this.getReal() + rhs.getReal();
    double imaginaryPart = this.getImaginary() + rhs.getImaginary();
    return new Complex(realPart, imaginaryPart);
}