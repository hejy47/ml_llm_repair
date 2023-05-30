## Fixed Function 1
protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    // check to avoid division by zero
    if (d == 2.0) {
        ret = Double.NaN; // or handle this case as appropriate for the application
    } else {
        // use mean
        ret = d / (d - 2.0);
    }
    return ret;
}