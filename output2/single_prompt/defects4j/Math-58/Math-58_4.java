## Fixed Function 1
public double[] fit() {
    final double[] guess = (new ParameterGuesser(getObservations())).getGuess();
    return fit(new Gaussian.Parametric(), guess);
}
## Fixed Function 2
public double[] fit() {
    final double[] guess = (new ParameterGuesser(getObservations())).getLmPar();
    return fit(new Gaussian.Parametric(), guess);
}