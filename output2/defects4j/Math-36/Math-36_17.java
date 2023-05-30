## Fixed Function 1
@Override
public double doubleValue() {
    if (denominator.doubleValue() == 0.0) {
        return Double.NaN;
    }
    double result = numerator.doubleValue() / denominator.doubleValue();
    return result;
}

## Fixed Function 2
@Override
public float floatValue() {
    if (denominator.floatValue() == 0.0f) {
        return Float.NaN;
    }
    float result = numerator.floatValue() / denominator.floatValue();
    return result;
}