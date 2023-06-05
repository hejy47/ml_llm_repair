## Fixed Function 1
public double cumulativeProbability(double x) throws MathException {
    final double dev = x - mean;
    try {
        return 0.5 * (1.0 + Erf.erf((dev) / (standardDeviation * Math.sqrt(2.0)))));
    } catch (MaxCountExceededException ex) {
        if (x < (mean - 20 * standardDeviation)) {
            // JDK 1.5 blows at 38
            return 0;
        } else if (x > (mean + 20 * standardDeviation)) {
            return 1;
        } else {
            throw new MathException("Maximum number of iterations exceeded", ex);
        }
    }
}

The changes made in the fixed function are as follows:

1. Replaced FastMath with Math as the former is not part of the standard Java library.
2. Replaced MaxIterationsExceededException with MaxCountExceededException as the former has been removed from the latest version of Apache Commons Math library.
3. Added a new MathException with the message "Maximum number of iterations exceeded" when the catch block is executed. This is to provide more information about the exception.