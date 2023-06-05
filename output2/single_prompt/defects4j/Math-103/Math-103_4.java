## Fixed Function 1
public double cumulativeProbability(double x) throws MathException {
    if (standardDeviation == 0) {
        if (x < mean)
            return 0.0;
        else if (x > mean)
            return 1.0;   
        else
            return 0.5;
    }
    return 0.5 * (1.0 + Erf.erf((x - mean) / (standardDeviation * Math.sqrt(2.0))));
}

Explanation:
The fixed function checks if the standard deviation is 0, in which case the distribution is degenerate and the result is returned directly.