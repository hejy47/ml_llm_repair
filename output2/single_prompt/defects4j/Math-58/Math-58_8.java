## Fixed Function 1
public double[] fit() {
    final double[] guess = new ParameterGuesser(getObservations()).getBestEstimates();
    return fit(new Gaussian.Parametric(), guess);
}