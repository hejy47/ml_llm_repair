## Fixed Function 1
public double cumulativeProbability(double x) throws MathException {
    if (standardDeviation == 0)
        throw new MathException("Standard deviation is zero");
    return 0.5 * (1.0 + Erf.erf((x - mean) / (standardDeviation * Math.sqrt(2.0))));
}

Explanation: The fixed version checks if the standard deviation is zero before calculating the cumulative probability. If it is zero, it throws an exception as dividing by zero would result in an error.