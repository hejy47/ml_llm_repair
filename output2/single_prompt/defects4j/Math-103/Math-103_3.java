## Fixed Function 1
public double cumulativeProbability(double x) throws MathException {
    return 0.5 * (1.0 + Erf.erf((x - getMean()) / (getStandardDeviation() * Math.sqrt(2.0))));
} 

Explanation: The function was using mean and standard deviation as a variable, but these values are not defined in the function. Instead, there were getter functions that were used to get the values. We need to call those functions to get the values.