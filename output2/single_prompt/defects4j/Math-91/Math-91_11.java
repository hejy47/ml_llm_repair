## Fixed Function 1
public int compareTo(Fraction object) {
    double nOd = this.numerator / (double)this.denominator;
    double dOn = object.numerator / (double)object.denominator;
    return (nOd < dOn) ? -1 : ((nOd > dOn) ? +1 : 0);
} 

Note: This fix assumes that the Fraction object contains numerator and denominator fields. If this is not the case, the fix may need to be adjusted accordingly.