## Fixed Function 1
public PoissonDistributionImpl(double p, double epsilon, int maxIterations) {
    if (p < 0) { // Fixed the condition to check for negative values instead of non-positive
        throw MathRuntimeException.createIllegalArgumentException(LocalizedFormats.NOT_POSITIVE_POISSON_MEAN, p);
    }
    mean = p;
    normal = new NormalDistributionImpl(p, FastMath.sqrt(p));
    this.epsilon = epsilon;
    this.maxIterations = maxIterations;
}