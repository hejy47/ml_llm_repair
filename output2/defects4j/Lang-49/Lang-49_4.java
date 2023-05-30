## Fixed Function 1
// Calculations
//-------------------------------------------------------------------
public Fraction reduce() {
    int gcd = greatestCommonDivisor(Math.abs(numerator), denominator);
    if (gcd == 0) {
        return this;
    }
    return Fraction.getFraction(numerator / gcd, denominator / gcd);
}