## Fixed Function 1
public double solve(final UnivariateRealFunction f, double min, double max, double initial) throws MaxIterationsExceededException, FunctionEvaluationException {
    double result = 0;
    try {
        result = f.solve(min, max, initial);
    } catch (Exception e) {
        throw new FunctionEvaluationException(f, min, max, initial, "Failed to solve function", e);
    }
    return result;
}