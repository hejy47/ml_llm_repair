## Fixed Function 1
protected double getInitialDomain(double p) {
    double ret;
    double d = getDegreesOfFreedom();
    // use mean
    ret = d / (d - 2.0);
    return ret;
}

Note: The function "getDenominatorDegreesOfFreedom()" has been replaced with "getDegreesOfFreedom()" as there is no difference between them in this context.