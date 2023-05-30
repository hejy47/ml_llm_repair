## Fixed Function 1
public PoissonDistributionImpl(double p, double epsilon, int maxIterations) {
    if (p < 0) { // changed from p <= 0 to p < 0
        throw MathRuntimeException.createIllegalArgumentException(LocalizedFormats.NOT_POSITIVE_POISSON_MEAN, p);
    }
    mean = p;
    normal = new NormalDistributionImpl(p, FastMath.sqrt(p));
    this.epsilon = epsilon;
    this.maxIterations = maxIterations;
}