## Fixed Function 1
private double calculateAsymptoticPValue(final double Umin, final int n1, final int n2) throws ConvergenceException, MaxCountExceededException {
    final int n1n2prod = n1 * n2;
    // make sure n1n2prod is greater than zero
    if (n1n2prod <= 0) {
        throw new IllegalArgumentException("n1 * n2 must be a positive number");
    }
    // http://en.wikipedia.org/wiki/Mann%E2%80%93Whitney_U#Normal_approximation
    final double EU = n1n2prod / 2.0;
    final double VarU = n1n2prod * (n1 + n2 + 1) / 12.0;
    final double z = (Umin - EU) / FastMath.sqrt(VarU);
    final NormalDistribution standardNormal = new NormalDistribution();
    return 2 * standardNormal.cumulativeProbability(-FastMath.abs(z));
}