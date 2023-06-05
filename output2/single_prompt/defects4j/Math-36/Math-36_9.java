## Fixed Function 1
@Override
public double doubleValue() {
    if(denominator.doubleValue() == 0) {
        return Double.POSITIVE_INFINITY;
    }
    double result = numerator.doubleValue() / denominator.doubleValue();
    return result;
}

## Fixed Function 2
@Override
public float floatValue() {
    if(denominator.floatValue() == 0) {
        return Float.POSITIVE_INFINITY;
    }
    float result = numerator.floatValue() / denominator.floatValue();
    return result;
}