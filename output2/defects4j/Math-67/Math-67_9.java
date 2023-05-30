## Fixed Function 1
public double getFunctionValue() {
    if (optimizer == null) {
        return Double.NaN;
    }
    return optimizer.getFunctionValue();
}

## Fixed Function 2
public double getResult() {
    if (optimizer == null) {
        return Double.NaN;
    }
    return optimizer.getResult();
}