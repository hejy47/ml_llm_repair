## Fixed Function 1
public double solve(final UnivariateRealFunction f, final double min, final double max, final double initial) throws MaxIterationsExceededException, FunctionEvaluationException {
    clearResult();
    verifyInterval(min, max);
    // return the initial guess if it is good enough
    double yInitial = f.value(initial);
    if (Math.abs(yInitial) <= functionValueAccuracy) {
        setResult(initial, 0);
        return result;
    }
    // return the first endpoint if it is good enough
    double yMin = f.value(min);
    if (Math.abs(yMin) <= functionValueAccuracy) {
        setResult(yMin, 0);
        return result;
    }
    // reduce interval if min and initial bracket the root
    if (yInitial * yMin < 0) {
        return solve(f, min, initial, yMin, yInitial);
    }
    // return the second endpoint if it is good enough
    double yMax = f.value(max);
    if (Math.abs(yMax) <= functionValueAccuracy) {
        setResult(yMax, 0);
        return result;
    }
    // reduce interval if initial and max bracket the root
    if (yInitial * yMax < 0) {
        return solve(f, initial, max, yInitial, yMax);
    }
    // full Brent algorithm starting with provided initial guess
    return solve(f, min, initial, max, yMin, yMax, yInitial);
}

## Fixed Function 2
private void verifyInterval(double lower, double upper) throws IllegalArgumentException {
    if (lower >= upper) {
        throw new IllegalArgumentException("Endpoints do not specify an interval: [" + lower + ", " + upper + "]");
    }
}