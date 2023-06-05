## Fixed Function 1
// Calculations
//-------------------------------------------------------------------
public Fraction reduce() {
    int gcd = Math.abs(greatestCommonDivisor(numerator, denominator)); // switch Math.abs() to numerator first
    if (gcd == 1) {
        return this;
    }
    return Fraction.getFraction(numerator / gcd, denominator / gcd);
}