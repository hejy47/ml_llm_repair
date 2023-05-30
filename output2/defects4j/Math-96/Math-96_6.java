## Fixed Function 1
public boolean equals(Object other) {
    boolean ret;
    if (this == other) {
        ret = true;
    } else if (other == null) {
        ret = false;
    } else if (!(other instanceof Complex)) {
        ret = false;
    } else {
        Complex rhs = (Complex) other;
        if (rhs.isNaN()) {
            ret = this.isNaN();
        } else {
            ret = (Double.doubleToRawLongBits(real) == Double.doubleToRawLongBits(rhs.getReal())) && (Double.doubleToRawLongBits(imaginary) == Double.doubleToRawLongBits(rhs.getImaginary()));
        }
    }
    return ret;
}