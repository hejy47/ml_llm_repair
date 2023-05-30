## Fixed Function 1
public boolean equals(Object other) {
    boolean ret;
    if (this == other) {
        ret = true;
    } else if (other == null) {
        ret = false;
    } else if (getClass() != other.getClass()) {
        // check that objects are of the same class
        ret = false;
    } else {
        try {
            Complex rhs = (Complex) other;
            if (rhs.isNaN()) {
                ret = this.isNaN();
            } else {
                ret = (Double.doubleToRawLongBits(real) == Double.doubleToRawLongBits(rhs.getReal())) && (Double.doubleToRawLongBits(imaginary) == Double.doubleToRawLongBits(rhs.getImaginary()));
            }
        } catch (ClassCastException ex) {
            // ignore exception
            ret = false;
        }
    }
    return ret;
}