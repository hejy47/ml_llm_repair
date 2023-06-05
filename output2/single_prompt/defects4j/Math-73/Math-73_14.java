## Fixed Function 1
public double solve(final UnivariateRealFunction f, final double min, 
                    final double max, final double initial) 
                    throws MaxIterationsExceededException, FunctionEvaluationException {
    clearResult();
    verifySequence(min, initial, max);
    double yInitial = f.value(initial);
    if (Math.abs(yInitial) <= functionValueAccuracy) {
        setResult(initial, 0);
        return result;
    }
    double yMin = f.value(min);
    if (Math.abs(yMin) <= functionValueAccuracy) {
        setResult(min, 0); // Fixed to set result to min instead of yMin
        return result;
    }
    if (yInitial * yMin < 0) {
        return solve(f, min, initial, yMin, yInitial, min, initial); // Fixed argument order
    }
    double yMax = f.value(max);
    if (Math.abs(yMax) <= functionValueAccuracy) {
        setResult(max, 0); // Fixed to set result to max instead of yMax
        return result;
    }
    if (yInitial * yMax < 0) {
        return solve(f, initial, max, yInitial, yMax, initial, max); // Fixed argument order
    }
    return solve(f, min, max, initial, yInitial, min, max); // Fixed argument order
}

## Fixed Function 2
private void verifyInterval(final double lower, final double upper) 
                            throws IllegalArgumentException {
    if (lower >= upper) { // Fixed to include equality
        throw new IllegalArgumentException("Invalid interval: [" + lower + ", " + upper + "]");
    }
}