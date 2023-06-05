## Fixed Function 1
public PoissonDistributionImpl(double p, double epsilon, int maxIterations) {
    if (p < 0) { // Changed from p <= 0 to p < 0 to include negative values of p.
        throw MathRuntimeException.createIllegalArgumentException(LocalizedFormats.NOT_POSITIVE_POISSON_MEAN, p);
    }
    mean = p;
    normal = new NormalDistributionImpl(p, FastMath.sqrt(p));
    this.epsilon = epsilon;
    this.maxIterations = maxIterations;
}