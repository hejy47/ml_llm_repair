## Fixed Function 1
public double[] fit() {
    final double[] guess = (new ParameterGuesser(getObservations())).guess();
    Gaussian.Parametric parametric = new Gaussian.Parametric();
    return fit(parametric, guess);
}