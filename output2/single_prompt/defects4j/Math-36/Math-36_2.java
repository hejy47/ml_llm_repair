## Fixed Function 1
@Override
public double doubleValue() {
    if(denominator.doubleValue() == 0.0){
        if(numerator.doubleValue() == 0.0){
            return Double.NaN; // both numerator and denominator are zero
        } else {
            return numerator.doubleValue() / 0.0; // numerator is non-zero
        }
    }
    return numerator.doubleValue() / denominator.doubleValue();
}

## Fixed Function 2
@Override
public float floatValue() {
    if(denominator.floatValue() == 0.0f){
        if(numerator.floatValue() == 0.0f){
            return Float.NaN; // both numerator and denominator are zero
        } else {
            return numerator.floatValue() / 0.0f; // numerator is non-zero
        }
    }
    return numerator.floatValue() / denominator.floatValue();
}