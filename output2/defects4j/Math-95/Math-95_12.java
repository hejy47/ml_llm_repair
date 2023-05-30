## Fixed Function 1
protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    // check for edge case where denominator degrees of freedom is less than 2
    if (d < 2) {
        ret = Double.NaN;
    } else {
        // use mean
        ret = d / (d - 2.0);
    }
    return ret;
}