## Fixed Function 1
public double cumulativeProbability(double x) throws MathException {
    final double dev = x - mean;
    double result;
    try {
        result = 0.5 * (1.0 + Erf.erf((dev) / (standardDeviation * FastMath.sqrt(2.0))));
    } catch (MaxIterationsExceededException ex) {
        if (x < (mean - 20 * standardDeviation)) {
            // JDK 1.5 blows at 38
            result = 0;
        } else if (x > (mean + 20 * standardDeviation)) {
            result = 1;
        } else {
            throw ex;
        }
    }
    return result;
}