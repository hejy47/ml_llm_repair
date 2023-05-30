## Fixed Function 1
@Override
public double doubleValue() {
    if(denominator.doubleValue() == 0) {
        throw new ArithmeticException("Cannot divide by zero");
    }
    double result = numerator.doubleValue() / denominator.doubleValue();
    return result;
}

## Fixed Function 2
@Override
public float floatValue() {
    if(denominator.floatValue() == 0) {
        throw new ArithmeticException("Cannot divide by zero");
    }
    float result = numerator.floatValue() / denominator.floatValue();
    return result;
}