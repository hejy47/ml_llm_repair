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
    return solve(f, min, max, initial);
}
 
private double solve(UnivariateRealFunction f, double x0, double x1, double y0, double y1) throws MaxIterationsExceededException, FunctionEvaluationException {
    double delta = y1 - y0;
    double x3 = x1;
    double y3 = y1;
    double x2 = x0;
    double y2 = y0;
    double yMin = y0;
    double xMin = Double.NaN;
    double oldDelta = Double.POSITIVE_INFINITY;
    for (int i = 0; i < maximalIterationCount; ++i) {
        if (y2 * y3 > 0) {
            // Switching endpoints, so change direction of search
            y2 = y0;
            y3 = y1;
            x2 = x0;
            x3 = x1;
            delta = y3 - y2;
            oldDelta = x3 - x2;
        }
        if (Math.abs(y3) < Math.abs(y2)) {
            // Swap 2 and 3
            double temp = y2;
            y2 = y3;
            y3 = temp;
            temp = x2;
            x2 = x3;
            x3 = temp;
        }
        // Compute new trial value.
        double xMid = x2 + oldDelta / delta * (y2 / (y2 - y3));
        double yMid;
        try {
            yMid = f.value(xMid);
        } catch (FunctionEvaluationException ex) {
            // Ignore the exception until later.  xMid is NaN and will trigger
            // an exception in any call to f or in any trial computation using xMid
            // NaN comparison is always false, so exit here.
            throw ex;
        }
        if (Double.isNaN(yMid)) {
            setResult(Double.NaN, i);
            return result;
        }
        // Check for convergence.
        double tolerance;
        if (yMid == 0) {
            // special case f(xMid) == 0
            setResult(xMid, i);
            return result;
        } else if (yMid * y2 < 0) {
            // f(xMid) and f(x2) have opposite signs
            y3 = yMid;
            x3 = xMid;
            delta = y3 - y2;
            tolerance = Math.abs(y3) * relativeAccuracy + absoluteAccuracy;
        } else if (yMid * y3 < 0) {
            // f(xMid) and f(x3) have opposite signs
            y2 = yMid;
            x2 = xMid;
            delta = y3 - y2;
            tolerance = Math.abs(y2) * relativeAccuracy + absoluteAccuracy;
        } else {
            // |f(xMid)| is greater or equal to |f(x2)| and |f(xMid)| is greater
            // or equal to |f(x3)|
            setResult(Double.NaN, i);
            return result;
        }
        if ((Double.isNaN(tolerance)) || (Double.isInfinite(tolerance))) {
            throw new MaxIterationsExceededException(maximalIterationCount);
        }
        if (Math.abs(yMid) <= tolerance) {
            setResult(xMid, i);
            return result;
        }
        oldDelta = x3 - x2;
    }
    throw new MaxIterationsExceededException(maximalIterationCount);
}