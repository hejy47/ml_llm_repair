## Fixed Function 1
public double cumulativeProbability(double x) throws MathException {
    double res = (x - mean) / (standardDeviation * Math.sqrt(2.0));
    return 0.5 * (1.0 + Erf.erf(res));
}