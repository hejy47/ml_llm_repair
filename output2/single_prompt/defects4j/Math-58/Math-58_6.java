## Fixed Function 1
public double[] fit() {
    final ParameterGuesser guesser = new ParameterGuesser(new Gaussian.Parametric(), getObservations());
    final double[] guess = guesser.getBestFitParameters();
    return fit(new Gaussian.Parametric(), guess);
}