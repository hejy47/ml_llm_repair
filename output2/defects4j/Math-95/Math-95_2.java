## Fixed Function 1
protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    if (d > 2.0) {
        ret = d / (d - 2.0);
    } else {
        ret = Double.NaN;
        System.err.println("Error: The degrees of freedom should be greater than 2.");
    }
    return ret;
}