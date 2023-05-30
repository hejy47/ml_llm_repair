## Fixed Function 1
public double cumulativeProbability(double x) throws MathException {
    double erf = Erf.erf((x - mean) / (standardDeviation * Math.sqrt(2.0)));
    if (Double.isNaN(erf))
      throw new MathException("Cannot calculate cumulative probability for non-finite input x");
    return 0.5 * (1.0 + erf);
}