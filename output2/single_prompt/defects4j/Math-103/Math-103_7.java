## Fixed Function 1
public double cumulativeProbability(double x) throws MathException {
    double scaleFactor = Math.sqrt(2) * standardDeviation;
    if (scaleFactor == 0) {
        if (x < mean) {
            return 0.0;
        } else {
            return 1.0;
        }
    }
    double scaledX = (x - mean) / scaleFactor;
    double result = (1 + Erf.erf(scaledX)) / 2;
    return result;
} 

Note: The fixed function includes additional checks for when standard deviation is equal to zero, as this input case was not originally handled properly. It also simplifies the calculation of the cumulative probability using the error function.