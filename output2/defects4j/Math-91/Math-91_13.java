## Fixed Function 1
public int compareTo(Fraction object) {
    double nOd = this.numerator / this.denominator;
    double dOn = object.numerator / object.denominator;
    return Double.compare(nOd, dOn);
}

Note: The fixed function assumes that the numerator and denominator are already simplified and stored in the object as instance variables. If the object's fraction is not stored in simplified form, additional code would need to be added to do so before calculating the double values.