## Fixed Function 1
// Calculations
//-------------------------------------------------------------------
public Fraction reduce() {
    int gcd = greatestCommonDivisor(Math.abs(numerator), Math.abs(denominator));
    if (gcd == 1) {
        return this;
    }
    int newNumerator = numerator / gcd;
    int newDenominator = denominator / gcd;
    if (newDenominator < 0) {
        newNumerator = -newNumerator;
        newDenominator = -newDenominator;
    }
    return new Fraction(newNumerator, newDenominator);
} 

//Explanation: The original code did not handle cases where the denominator was negative, which could result in an incorrect reduced fraction. The fix checks for a negative denominator and ensures that the numerator and denominator are both negative or both positive after reducing. It also returns a new Fraction object instead of using the static getFraction method.