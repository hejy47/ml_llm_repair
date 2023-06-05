## Fixed Function 1
@Override
public double doubleValue() {
    double result = numerator.doubleValue() / denominator.doubleValue();

    // Check for NaN results
    if (Double.isNaN(result)) {
        throw new ArithmeticException("Invalid division: numerator and/or denominator out of range");
    }

    return result;
}


## Fixed Function 2
@Override
public float floatValue() {
    float result = numerator.floatValue() / denominator.floatValue();

    // Check for NaN results
    if (Float.isNaN(result)) {
        throw new ArithmeticException("Invalid division: numerator and/or denominator out of range");
    }

    return result;
}