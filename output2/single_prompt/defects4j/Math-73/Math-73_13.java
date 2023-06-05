## Fixed Function 1
public double solve(final UnivariateRealFunction f, final double min, final double max, final double initial) throws MaxIterationsExceededException, FunctionEvaluationException {
    clearResult();
    verifySequence(min, initial, max);
    // return the initial guess if it is good enough
    double yInitial = f.value(initial);
    if (Math.abs(yInitial) <= functionValueAccuracy) {
        setResult(initial, 0);
        return result;
    }
    // return the first endpoint if it is good enough
    double yMin = f.value(min);
    if (Math.abs(yMin) <= functionValueAccuracy) {
        setResult(min, 0);
        return result;
    }
    // reduce interval if min and initial bracket the root
    if (yInitial * yMin < 0) {
        return solve(f, min, initial, yMin, yInitial);
    }
    // return the second endpoint if it is good enough
    double yMax = f.value(max);
    if (Math.abs(yMax) <= functionValueAccuracy) {
        setResult(max, 0);
        return result;
    }
    // reduce interval if initial and max bracket the root
    if (yInitial * yMax < 0) {
        return solve(f, initial, max, yInitial, yMax);
    }
    // full Brent algorithm starting with provided initial guess
    return solve(f, min, max, initial, yInitial, min, yMin, max, yMax);
}

## Fixed Function 2
private double findTestPoint(final double lo, final double hi) {
    // handle special case of lo or hi being NaN
    if (!(lo <= hi)) {
        if (Double.isNaN(lo) && Double.isNaN(hi)) {
            // both interval endpoints are NaN, treat NaN as a "large" double
            return Double.MAX_VALUE;
        }
        // one endpoint is NaN and the other isn't, return the non-NaN value
        return Double.isNaN(lo) ? hi : lo;
    }
    // neither endpoint is NaN, continue with regular logic
    return (lo + hi) * 0.5;
}