## Fixed Function 1
protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    if (d > 2.0) {
        // use mean
        ret = d / (d - 2.0);
    } else {
        ret = 1.0;
    }
    return ret;
}

Explanation: 
The buggy function does not handle the case when the denominator degrees of freedom is less than or equal to 2. In this case, the returned value should be 1.0. The fixed function adds an if statement to handle this special case and return 1.0.