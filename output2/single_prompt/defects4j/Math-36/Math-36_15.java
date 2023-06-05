## Fixed Function 1
@Override
public double doubleValue() {
    if (denominator.doubleValue() == 0) {
        if (numerator.doubleValue() > 0)
            return Double.POSITIVE_INFINITY;
        else if (numerator.doubleValue() < 0)
            return Double.NEGATIVE_INFINITY;
        else
            return Double.NaN;
    }
    return numerator.doubleValue() / denominator.doubleValue();
}

## Fixed Function 2
@Override
public float floatValue() {
    if (denominator.floatValue() == 0) {
        if (numerator.floatValue() > 0)
            return Float.POSITIVE_INFINITY;
        else if (numerator.floatValue() < 0)
            return Float.NEGATIVE_INFINITY;
        else
            return Float.NaN;
    }
    return numerator.floatValue() / denominator.floatValue();
}