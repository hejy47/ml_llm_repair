## Fixed Function 1
public double cumulativeProbability(double x) throws MathException {
    final double dev = x - mean;
    try {
        return 0.5 * (1.0 + Erf.erf(dev / (standardDeviation * FastMath.sqrt(2.0))));
    } catch (MaxIterationsExceededException ex) {
        if (x < (mean - 20 * standardDeviation)) {
            // Too small to represent
            return 0.0;
        } else if (x > (mean + 20 * standardDeviation)) {
            // Too large to represent
            return 1.0;
        } else {
            throw new MathInternalError(ex);
        }
    }
}