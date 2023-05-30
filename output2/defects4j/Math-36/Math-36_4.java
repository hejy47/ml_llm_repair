## Fixed Function 1
@Override
public double doubleValue() {
    double result = numerator.doubleValue() / denominator.doubleValue();
    if (Double.isInfinite(result) && ! Double.isNaN(result)) {
        result = Double.POSITIVE_INFINITY;
    }
    if (Double.isNaN(result)) {
        throw new ArithmeticException("Undefined");
    }
    return result;
}

## Fixed Function 2
@Override
public float floatValue() {
    float result = numerator.floatValue() / denominator.floatValue();
    if (Float.isInfinite(result) && ! Float.isNaN(result)) {
        result = Float.POSITIVE_INFINITY;
    }
    if (Float.isNaN(result)) {
        throw new ArithmeticException("Undefined");
    }
    return result;
}