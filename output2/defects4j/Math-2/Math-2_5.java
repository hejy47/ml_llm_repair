## Fixed Function 1
public double getNumericalMean() {
    if (getPopulationSize() == 0) {
        return 0.0;
    }
    return (double) getNumberOfSuccesses() / (double) getPopulationSize();
}