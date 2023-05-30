## Fixed Function 1
protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    // use mean
    if (d > 2.0) { // check if denominator degrees of freedom is greater than 2
        ret = d / (d - 2.0);
        return ret;
    }
    return Double.POSITIVE_INFINITY; // return positive infinity if denominator degrees of freedom is less than or equal to 2
}