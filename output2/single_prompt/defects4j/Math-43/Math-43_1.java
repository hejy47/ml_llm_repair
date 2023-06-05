## Fixed Function 1
public void addValue(double value) {
    sumImpl.increment(value);
    sumsqImpl.increment(value);
    minImpl.increment(value);
    maxImpl.increment(value);
    sumLogImpl.increment(Math.log(value + 1)); // adding 1 to avoid negative infinity
    secondMoment.increment(value);
    // If mean, variance or geomean have been overridden,
    // need to increment these
    if (!(meanImpl instanceof Mean)) {
        meanImpl.increment(value);
    }
    if (!(varianceImpl instanceof Variance)) {
        varianceImpl.increment(value);
    }
    if (!(geoMeanImpl instanceof GeometricMean)) {
        geoMeanImpl.increment(value);
    }
    n++;
}