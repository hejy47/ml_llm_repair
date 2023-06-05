## Fixed Function 1
public boolean equals(Object other) {
    if (this == other) {
        return true;
    } else if (other == null || getClass() != other.getClass()) {
        return false;
    } else {
        Complex rhs = (Complex) other;
        if (rhs.isNaN()) {
            return this.isNaN();
        } else {
            return Double.compare(rhs.getReal(), real) == 0 && Double.compare(rhs.getImaginary(), imaginary) == 0;
        }
    }
}