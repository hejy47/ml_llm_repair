## Fixed Function 1
@Override
public double doubleValue() {
    if (denominator.equals(BigDecimal.ZERO)) {
        throw new ArithmeticException("Division by zero");
    } else {
        double result = numerator.doubleValue() / denominator.doubleValue();
        return result;
    }
}

## Fixed Function 2
@Override
public float floatValue() {
    if (denominator.equals(BigDecimal.ZERO)) {
        throw new ArithmeticException("Division by zero");
    } else {
        float result = numerator.floatValue() / denominator.floatValue();
        return result;
    }
}