## Fixed Function 1
public int compareTo(Fraction object) {
    double nOd = this.numerator * 1.0 / this.denominator;
    double dOn = object.numerator * 1.0 / object.denominator;
    return (nOd < dOn) ? -1 : ((nOd > dOn) ? +1 : 0);
}