## Fixed Function 1
@Override
public double doubleValue() {
    double result = numerator.doubleValue() / denominator.doubleValue();
    if (Double.isNaN(result) || Double.isInfinite(result)) {
        return 0.0; // return a default value or throw an exception
    }
    return result;
}

## Fixed Function 2
@Override
public float floatValue() {
    float result = numerator.floatValue() / denominator.floatValue();
    if (Float.isNaN(result) || Float.isInfinite(result)) {
        return 0.0f; // return a default value or throw an exception
    }
    return result;
}