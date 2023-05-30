## Fixed Function 1
public static double[] bracket(UnivariateRealFunction function, double initial, double lowerBound, double upperBound, int maximumIterations) throws ConvergenceException, FunctionEvaluationException {
    if (function == null) {
        throw new IllegalArgumentException("function is null");
    }
    if (maximumIterations <= 0) {
        throw new IllegalArgumentException("bad value for maximum iterations number: {0}", maximumIterations);
    }
    if (initial < lowerBound || initial > upperBound || lowerBound >= upperBound) {
        throw new IllegalArgumentException("invalid bracketing parameters:  lower bound={0},  initial={1}, upper bound={2}", lowerBound, initial, upperBound);
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
    } while ((fa * fb > 0.0) && (numIterations < maximumIterations) && ((a > lowerBound) || (b < upperBound)));
    if (fa * fb >= 0.0) {
        throw new ConvergenceException("number of iterations=%d, maximum iterations=%d, " + "initial=%f, lower bound=%f, upper bound=%f, final a value=%f, " + "final b value=%f, f(a)=%f, f(b)=%f", numIterations, maximumIterations, initial, lowerBound, upperBound, a, b, fa, fb);
    }
    return new double[] { a, b };
}