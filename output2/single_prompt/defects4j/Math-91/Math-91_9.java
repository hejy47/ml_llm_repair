## Fixed Function 1
public int compareTo(Fraction object) {
    double nOd = this.numerator / (double) this.denominator;
    double dOn = object.numerator / (double) object.denominator;
    return Double.compare(nOd, dOn);
}