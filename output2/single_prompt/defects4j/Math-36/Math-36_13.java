## Fixed Function 1
@Override
public double doubleValue() {
    if (denominator.doubleValue() == 0.0) {
        return numerator.doubleValue() > 0 ? Double.POSITIVE_INFINITY :
                numerator.doubleValue() < 0 ? Double.NEGATIVE_INFINITY :
                        Double.NaN;
    }
    double result = numerator.doubleValue() / denominator.doubleValue();
    return result;
}

## Fixed Function 2
@Override
public float floatValue() {
    if (denominator.floatValue() == 0.0f) {
        return numerator.floatValue() > 0 ? Float.POSITIVE_INFINITY :
                numerator.floatValue() < 0 ? Float.NEGATIVE_INFINITY :
                        Float.NaN;
    }
    float result = numerator.floatValue() / denominator.floatValue();
    return result;
}