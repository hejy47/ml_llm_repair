## Fixed Function 1
public boolean equals(Object other) {
    boolean ret;
    if (this == other) {
        ret = true;
    } else if (other == null || getClass() != other.getClass()) {
        ret = false;
    } else {
        try {
            Complex rhs = (Complex) other;
            if (rhs.isNaN()) {
                ret = this.isNaN();
            } else {
                ret = Double.compare(rhs.getReal(), real) == 0 && Double.compare(rhs.getImaginary(), imaginary) == 0;
            }
        } catch (ClassCastException ex) {
            // ignore exception
            ret = false;
        }
    }
    return ret;
}