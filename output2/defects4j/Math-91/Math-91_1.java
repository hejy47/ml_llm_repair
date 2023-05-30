## Fixed Function 1
public int compareTo(Fraction object) {
    double nOd = this.numerator / (double) this.denominator;
    double dOn = object.numerator / (double) object.denominator;
    return (nOd < dOn) ? -1 : ((nOd > dOn) ? +1 : 0);
} 

Note: The fix uses the numerator and denominator of the fractions to calculate their double values for comparison instead of calling the doubleValue() method, which may introduce floating point precision errors.