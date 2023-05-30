## Fixed Function 1
public double[] fit() {
    final double[] guess = (new ParameterGuesser(getObservations())).getParameters();
    return fit(new Gaussian.Parametric(), guess);
}