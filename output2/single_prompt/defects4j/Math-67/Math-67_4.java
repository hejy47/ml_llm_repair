## Fixed Function 1
public double getFunctionValue() {
    Optimizer optimizer = new Optimizer();
    optimizer.setFunction(this);
    return optimizer.getFunctionValue();
}

## Fixed Function 2
public double getResult() {
    Optimizer optimizer = new Optimizer();
    optimizer.setFunction(this);
    optimizer.run();
    return optimizer.getResult();
}