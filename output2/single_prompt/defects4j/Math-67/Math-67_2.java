## Fixed Function 1
public double getFunctionValue() {
    return optimizer.getOptimum().getValue();
}

## Fixed Function 2
public double getResult() {
    return optimizer.getOptimum().getPoint()[0];
}