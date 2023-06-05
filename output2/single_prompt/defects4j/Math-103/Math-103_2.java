## Fixed Function 1
public double cumulativeProbability(double x) throws MathException {
    NormalDistribution normal = new NormalDistribution(mean, standardDeviation);
    return normal.cumulativeProbability(x);
}