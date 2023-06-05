## Fixed Function 1
public double[] fit() {
    final double[] guess = (new ParameterGuesser(new Gaussian.Parametric(),getObservations())).guess();
    return fit(new Gaussian.Parametric(), guess);
}