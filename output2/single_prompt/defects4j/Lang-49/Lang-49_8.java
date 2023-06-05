## Fixed Function 1
// Calculations
//-------------------------------------------------------------------
public Fraction reduce() {
    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator)); // need to take absolute values for correct gcd calculation
    if (gcd == 1) {
        return this;
    }
    return Fraction.getFraction(numerator / gcd, denominator / gcd);
}