## Fixed Function 1
protected double getInitialDomain(double p) {
    double ret;
    double d = getDenominatorDegreesOfFreedom();
    // use mean
    if(d > 2.0){
      ret = d / (d - 2.0);
    }
    else{
      ret = Double.POSITIVE_INFINITY;
    }
    return ret;
}