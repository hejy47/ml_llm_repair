## Fixed Function 1
@Override
public double doubleValue() {
    double result = numerator.doubleValue() / denominator.doubleValue();
    if (Double.isInfinite(result) || Double.isNaN(result)) {
        // Numerator and/or denominator must be out of range:
        // Calculate how far to shift them to put them in range.
        if (numerator != BigDecimal.ZERO) {
            int exponent = numerator.scale() - denominator.scale();
            if (exponent > 0) {
                denominator = denominator.scaleByPowerOfTen(exponent);
            } else {
                numerator = numerator.scaleByPowerOfTen(-exponent);
            }
            result = numerator.doubleValue() / denominator.doubleValue();
        }
    }
    return result;
}

## Fixed Function 2
@Override
public float floatValue() {
    float result = numerator.floatValue() / denominator.floatValue();
    if (Float.isInfinite(result) || Float.isNaN(result)) {
        // Numerator and/or denominator must be out of range:
        // Calculate how far to shift them to put them in range.
        if (numerator != BigDecimal.ZERO) {
            int exponent = numerator.scale() - denominator.scale();
            if (exponent > 0) {
                denominator = denominator.scaleByPowerOfTen(exponent);
            } else {
                numerator = numerator.scaleByPowerOfTen(-exponent);
            }
            result = numerator.floatValue() / denominator.floatValue();
        }
    }
    return result;
}