## Fixed Function 1
protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    // use mean
    ret = 2.0 / (d - 2.0);
    return ret;
}