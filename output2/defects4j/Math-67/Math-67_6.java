## Fixed Function 1
public double getFunctionValue() {
    if (optimizer == null) {
        throw new IllegalStateException("Optimizer has not been initialized.");
    }
    return optimizer.getFunctionValue();
}

## Fixed Function 2
public double getResult() {
    if (optimizer == null) {
        throw new IllegalStateException("Optimizer has not been initialized.");
    }
    return optimizer.getResult();
}