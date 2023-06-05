## Fixed Function 1
public double solve(final UnivariateRealFunction f, double min, double max, double initial) throws MaxIterationsExceededException, FunctionEvaluationException {
    double result = 0;
    // Use Brent's method for root finding
    try {
        result = new BrentSolver().solve(100, f, min, max, initial);
    } catch (MaxIterationsExceededException e) {
        throw new MaxIterationsExceededException(100);
    } catch (FunctionEvaluationException e) {
        throw new FunctionEvaluationException(f, initial);
    }
    return result;
}