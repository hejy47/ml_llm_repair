## Fixed Function 1
public double getFunctionValue() {
    if (optimizer != null) {
        return optimizer.getFunctionValue();
    } else {
        throw new IllegalStateException("Optimizer has not been initialized.");
    }
}

## Fixed Function 2
public double getResult() {
    if (optimizer != null) {
        return optimizer.getResult();
    } else {
        throw new IllegalStateException("Optimizer has not been initialized.");
    }
}