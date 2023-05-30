## Fixed Function 1
public double cumulativeProbability(double x) throws MathException {
    final double dev = x - mean;
    try {
        return 0.5 * (1.0 + Erf.erf((dev) / (standardDeviation * FastMath.sqrt(2.0)))));
    } catch (MaxIterationsExceededException ex) {
        if (x < (mean - 20 * standardDeviation)) {
            // JDK 1.5 blows at 38
            return 0.0; // explicitly return double 0.0 instead of integer 0
        } else if (x > (mean + 20 * standardDeviation)) {
            return 1.0; // explicitly return double 1.0 instead of integer 1
        } else {
            throw new MathException("Maximum number of iterations exceeded"); // throw a new MathException 
        }
    }
}