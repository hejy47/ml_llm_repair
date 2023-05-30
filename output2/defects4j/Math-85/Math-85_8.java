## Fixed Function 1
public static double[] bracket(UnivariateRealFunction function, double initial, double lowerBound, double upperBound, int maximumIterations) throws ConvergenceException, FunctionEvaluationException {
    if (function == null) {
        throw new MathIllegalArgumentException("function is null");
    }
    if (maximumIterations <= 0) {
        throw new MathIllegalArgumentException("bad value for maximum iterations number: {0}", maximumIterations);
    }
    if (initial < lowerBound || initial > upperBound || lowerBound >= upperBound) {
        throw new MathIllegalArgumentException("invalid bracketing parameters:  lower bound={0},  initial={1}, upper bound={2}", lowerBound, initial, upperBound);
    }
    double a = initial;
    double b = initial;
    double fa;
    double fb;
    int numIterations = 0;
    do {
        a = Math.max(a - 1.0, lowerBound);
        b = Math.min(b + 1.0, upperBound);
        fa = function.value(a);
        fb = function.value(b);
        numIterations++;
    } while ((fa * fb > 0.0) && (numIterations < maximumIterations) && ((a > lowerBound) && (b < upperBound)));
    if (fa * fb >= 0.0) {
        throw new ConvergenceException(new LocalizedFormats("FAILED_BRACKETING"),
                             numIterations, maximumIterations, initial, lowerBound, upperBound, a, b, fa, fb);
    }
    return new double[] { a, b };
}