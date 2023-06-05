## Fixed Function 1
public double cumulativeProbability(double x) throws MathException {
    NormalDistribution norm = new NormalDistribution(mean, standardDeviation);
    return norm.cumulativeProbability(x);
} 

# Explanation
The original function uses the error function (Erf) to calculate the cumulative probability, but this function is not sufficiently accurate for some values of x, causing incorrect results. The fixed function replaces the use of the error function with the use of the NormalDistribution class from the Apache Math library, which provides a more accurate implementation for calculating the cumulative probability.