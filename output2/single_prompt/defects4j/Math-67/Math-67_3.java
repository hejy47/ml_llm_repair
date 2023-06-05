## Fixed Function 1
public double getFunctionValue() {
    if (optimizer != null) {
        return optimizer.getFunctionValue();
    }
    return Double.NaN;
}

## Fixed Function 2
public double getResult() {
    if (optimizer != null) {
        return optimizer.getResult();
    }
    return Double.NaN;
}