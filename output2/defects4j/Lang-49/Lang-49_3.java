## Fixed Function 1
// Calculations
//-------------------------------------------------------------------
public Fraction reduce() {
    int gcd = Math.abs(greatestCommonDivisor(numerator, denominator));
    if (gcd == 1) {
        return this;
    }
    return Fraction.getFraction(numerator / gcd, denominator / gcd);
} 

// In the original function, the greatestCommonDenominator method wasn't returning the correct value for negative numbers. By taking the absolute value of the arguments before passing them to the method, it ensures that the correct gcd is calculated.