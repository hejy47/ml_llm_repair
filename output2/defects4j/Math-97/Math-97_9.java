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
        // check if either value is close to a zero
        double yMinAbs = Math.abs(yMin);
        double yMaxAbs = Math.abs(yMax);
        if (yMinAbs < yMaxAbs) {
          ret = min;
        } else {
          ret = max;
        }
    } else {
        // solve using only the first endpoint as initial guess
        ret = solve(min, yMin, max, yMax, min, yMin);
        // either min or max is a root
    }
    return ret;
}