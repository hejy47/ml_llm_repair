## Fixed Function 1
Assuming the `optimizer` variable is of type `UnivariateOptimizer`, this buggy function should be fixed by providing a value to optimize in the `getFunctionValue()` method:

public double getFunctionValue(double x) {
    return optimizer.getFunctionValue();
}

## Fixed Function 2
Assuming the `optimizer` variable is of type `UnivariateOptimizer`, this buggy function should be fixed by calling `optimize()` before trying to get the optimized value:

public double getResult() {
    optimizer.optimize();
    return optimizer.getResult();
}