## Fixed Function 1
public PoissonDistributionImpl(double mean, double epsilon, int maxIterations) {
    if (mean <= 0) {
        throw MathRuntimeException.createIllegalArgumentException(LocalizedFormats.NOT_POSITIVE_POISSON_MEAN, mean);
    }
    this.mean = mean;
    normal = new NormalDistributionImpl(mean, FastMath.sqrt(mean));
    this.epsilon = epsilon;
    this.maxIterations = maxIterations;
}