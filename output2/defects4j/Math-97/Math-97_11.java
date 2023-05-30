## Fixed Function 1
public double solve(double min, double max) throws MaxIterationsExceededException, FunctionEvaluationException {
    clearResult();
    verifyInterval(min, max);
    double ret = Double.NaN;
    double yMin = f.value(min);
    double yMax = f.value(max);
    // Verify bracketing
    double sign = yMin * yMax;
    if (sign >= 0) {
        if (Math.abs(yMin) < Math.abs(yMax)) {
            if (yMin == 0) {
                return min;
            }
            ret = solve(min, yMin, max, yMax, min, yMin);
        } else {
            if (yMax == 0) {
                return max;
            }
            ret = solve(min, yMin, max, yMax, max, yMax);
        }
    } else {
        // solve using only the first endpoint as initial guess
        ret = solve(min, yMin, max, yMax, min, yMin);
        // either min or max is a root
    }
    return ret;
}