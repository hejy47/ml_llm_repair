## Fixed Function 1
private double calculateAsymptoticPValue(final double Umin, final int n1, final int n2) throws ConvergenceException, MaxCountExceededException {
    final int n1n2prod = n1 * n2;
    // http://en.wikipedia.org/wiki/Mann%E2%80%93Whitney_U#Normal_approximation
    final double EU = n1n2prod / 2.0;
    final double VarU = n1n2prod * (n1 + n2 + 1) / 12.0;
    final double z = (Umin - EU) / FastMath.sqrt(VarU);
    final NormalDistribution standardNormal = new NormalDistribution(0, 1);
    return 2 * (1 - standardNormal.cumulativeProbability(Math.abs(z)));
}

The fix for the above function is to return 2 times the complement (1 - CDF) of the standard normal distribution at z, where z is calculated as Umin - EU / sqrt(VarU). This is because we want to find the probability of obtaining a test statistic as extreme as Umin assuming that the null hypothesis is true.