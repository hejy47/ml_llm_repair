## Fixed Function 1
public PoissonDistributionImpl(double p, double epsilon, int maxIterations) {
    if (p < 0) { // fixed to exclude p = 0 case
        throw MathRuntimeException.createIllegalArgumentException(LocalizedFormats.NEGATIVE_POISSON_MEAN, p);
    }
    mean = p;
    normal = new NormalDistributionImpl(p, FastMath.sqrt(p));
    this.epsilon = epsilon;
    this.maxIterations = maxIterations;
}