## Fixed Function 1
public boolean equals(Object other) {
    if(this == other) {
        return true;
    }
    if (!(other instanceof Complex)) {
        return false;
    }
    Complex rhs = (Complex) other;
    if (rhs.isNaN()) {
        return this.isNaN();
    }
    return (Double.compare(real, rhs.getReal()) == 0) && (Double.compare(imaginary, rhs.getImaginary()) == 0);
} 

Changes made:
- Changed the nested if-else statements to two separate if statements for better readability
- Used instanceof to check if the object being compared is of type Complex or not
- Used Double.compare() instead of comparing bit values using Double.doubleToRawLongBits() for comparing double values as it gives more accuracy and avoids issues with NaN values.