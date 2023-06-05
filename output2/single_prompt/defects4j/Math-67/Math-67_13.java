## Fixed Function 1
public double getFunctionValue() {
    return optimizer.getCost();
}

## Fixed Function 2
public double getResult() {
    return optimizer.getCurrentSolution().getCost();
}