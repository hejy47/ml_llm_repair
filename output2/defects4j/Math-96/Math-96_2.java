## Fixed Function 1
public boolean equals(Object other) {
    if (this == other) {
        return true;
    }
    if (!(other instanceof Complex)) {
        return false;
    }
    Complex rhs = (Complex) other;
    if (rhs.isNaN()) {
        return this.isNaN();
    }
    return Double.doubleToRawLongBits(real) == Double.doubleToRawLongBits(rhs.getReal()) && Double.doubleToRawLongBits(imaginary) == Double.doubleToRawLongBits(rhs.getImaginary());
}