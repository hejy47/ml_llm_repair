## Fixed Function 1
public double solve(final UnivariateRealFunction f, double min, double max, double initial) throws MaxIterationsExceededException, FunctionEvaluationException {
    return BrentSolverFactory.newInstance().solve(f, min, max, initial);
}