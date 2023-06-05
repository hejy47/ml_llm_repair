## Fixed Function 1
// Calculations
//-------------------------------------------------------------------
public Fraction reduce() {
    int gcd = greatestCommonDivisor(Math.abs(numerator), denominator);
    if (gcd == 1) {
        return this;
    }
    int reducedNumerator = numerator / gcd;
    int reducedDenominator = denominator / gcd;
    if (reducedDenominator < 0) {
        reducedNumerator *= -1;
        reducedDenominator *= -1;
    }
    return Fraction.getFraction(reducedNumerator, reducedDenominator);
} 

// This fix reduces the fraction to its lowest terms and ensures that the negative sign is in the numerator.