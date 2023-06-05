## Fixed Function 1
protected double getInitialDomain(double p) {
    double ret;
    double d = getDegreesOfFreedom();
    // use mean
    ret = 1.0 / (d - 2.0);
    return ret;
}