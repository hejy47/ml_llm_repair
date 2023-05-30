## Fixed Function 1
protected double getInitialDomain(double p) {
    double ret = Double.NaN;
    double d = getDenominatorDegreesOfFreedom();
    if (d > 2.0) {
        // use mean
        ret = d / (d - 2.0);
    }
    return ret;
}