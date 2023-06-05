## Fixed Function 1
protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    if (d <= 2.0) {
        ret = Double.NaN;
    } else {
        // use mean
        ret = d / (d - 2.0);
    }
    return ret;
}