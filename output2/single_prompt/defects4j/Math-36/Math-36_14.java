## Fixed Function 1
@Override
public double doubleValue() {
    if (denominator.doubleValue() == 0) {
        return numerator.doubleValue() > 0 ? Double.POSITIVE_INFINITY : Double.NEGATIVE_INFINITY;
    }
    double result = numerator.doubleValue() / denominator.doubleValue();
    return result;
}

## Fixed Function 2
@Override
public float floatValue() {
    if (denominator.floatValue() == 0) {
        return numerator.floatValue() > 0 ? Float.POSITIVE_INFINITY : Float.NEGATIVE_INFINITY;
    }
    float result = numerator.floatValue() / denominator.floatValue();
    return result;
}