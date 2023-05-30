## Fixed Function 1
@Override
public double doubleValue() {
    double result;
    if(denominator.doubleValue()==0){
        if(numerator.doubleValue()>0){
            result = Double.POSITIVE_INFINITY;
        }else if(numerator.doubleValue()<0){
            result = Double.NEGATIVE_INFINITY;
        }else{
            result = Double.NaN;
        }
    }else{
        result = numerator.doubleValue() / denominator.doubleValue();
    }
    return result;
}

## Fixed Function 2
@Override
public float floatValue() {
    float result;
    if(denominator.floatValue()==0){
        if(numerator.floatValue()>0){
            result = Float.POSITIVE_INFINITY;
        }else if(numerator.floatValue()<0){
            result = Float.NEGATIVE_INFINITY;
        }else{
            result = Float.NaN;
        }
    }else{
        result = numerator.floatValue() / denominator.floatValue();
    }
    return result;
}