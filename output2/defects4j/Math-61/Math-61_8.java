## Fixed Function 1
public PoissonDistributionImpl(double p, double epsilon, int maxIterations) {
    if (p < 0) { //change condition to allow for Poisson distributions with mean = 0
        throw MathRuntimeException.createIllegalArgumentException(LocalizedFormats.NOT_POSITIVE_POISSON_MEAN, p);
    }
    mean = p;
    normal = new NormalDistributionImpl(p, FastMath.sqrt(p));
    this.epsilon = epsilon;
    this.maxIterations = maxIterations;
}