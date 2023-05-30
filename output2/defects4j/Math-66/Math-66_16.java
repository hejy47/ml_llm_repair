## Fixed Function 1
public BrentOptimizer() {
    setMaxEvaluations(Integer.MAX_VALUE);
    setMaximalIterationCount(100);
    setAbsoluteAccuracy(1E-10);
    setRelativeAccuracy(1.0e-14);
    setResult(null);
}

## Fixed Function 2
protected double doOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
    return Double.NaN;
}

## Fixed Function 3
public double optimize(final UnivariateRealFunction f, final GoalType goalType, final double min, final double max, final double startValue) throws MaxIterationsExceededException, FunctionEvaluationException {
    clearResult();
    return localMin(getGoalType() == GoalType.MINIMIZE, f, goalType, min, startValue, max, getRelativeAccuracy(), getAbsoluteAccuracy(), Double.NaN, Double.NaN);
}

## Fixed Function 4
public double optimize(final UnivariateRealFunction f, final GoalType goalType, final double min, final double max) throws MaxIterationsExceededException, FunctionEvaluationException {
    return optimize(f, goalType, min, max, min + GOLDEN_SECTION * (max - min));
}

## Fixed Function 5
private double localMin(boolean isMinim, UnivariateRealFunction f, GoalType goalType, double lo, double mid, double hi, double eps, double t, double d1, double d2) throws MaxIterationsExceededException, FunctionEvaluationException {
    if (eps <= 0 || t <= 0) {
        throw new NotStrictlyPositiveException(new Double[] {eps, t});
    }
    double a, b;
    if (lo < hi) {
        a = lo;
        b = hi;
    } else {
        a = hi;
        b = lo;
    }
    double x = mid;
    double v = x;
    double w = x;
    double d = Double.NaN;
    double e = Double.NaN;
    double fx = computeObjectiveValue(f, x);
    if (goalType == GoalType.MAXIMIZE) {
        fx = -fx;
    }
    double fv = fx;
    double fw = fx;
    int count = 0;
    while (count < maximalIterationCount) {
        double m = 0.5 * (a + b);
        final double tol1 = eps * Math.abs(x) + t;
        final double tol2 = 2 * tol1;
        if (Math.abs(x - m) > tol2 - 0.5 * (b - a)) {
            double p = 0;
            double q = 0;
            double r = 0;
            double u = 0;
            if (Math.abs(e) > tol1) {
                r = (x - w) * (fx - fv);
                q = (x - v) * (fx - fw);
                p = (x - v) * q - (x - w) * r;
                q = 2 * (q - r);
                if (q > 0) {
                    p = -p;
                } else {
                    q = -q;
                }
                r = e;
                e = d;
                if (!(Math.abs(p) < Math.abs(0.5 * q * r)) && !(p > q * (a - x)) && !(p < q * (b - x))) {
                    d = GOLDEN_SECTION * ((e >= 0) ? a - x : b - x);
                } else {
                    d = p / q;
                    u = x + d;
                    if (u - a < tol2 || b - u < tol2) {
                        d = tol1 * ((x <= m) ? 1 : -1);
                    }
                }
            } else {
                d = GOLDEN_SECTION * ((e >= 0) ? a - x : b - x);
            }
            if (!(Math.abs(d) >= tol1)) {
                u = x + ((d >= 0) ? tol1 : -tol1);
            } else {
                u = x + d;
            }
            double fu = computeObjectiveValue(f, u);
            if (goalType == GoalType.MAXIMIZE) {
                fu = -fu;
            }
            if (fu <= fx) {
                if (u < x) {
                    b = x;
                } else {
                    a = x;
                }
                v = w;
                fv = fw;
                w = x;
                fw = fx;
                x = u;
                fx = fu;
            } else {
                if (u < x) {
                    a = u;
                } else {
                    b = u;
                }
                if (fu <= fw || w == x) {
                    v = w;
                    fv = fw;
                    w = u;
                    fw = fu;
                } else if ((fu <= fv && v != x && v != w) || fv == x || fv == w) {
                    v = u;
                    fv = fu;
                }
            }
            if (!(Math.abs(d)<tol1&&Math.abs(u-x)<tol1)) {
                e = d;
            } else {
                e = ((x < m) ? b-x : a-x);
                d = GOLDEN_SECTION * e;
            }
        } else {
            setResult(x, (goalType == GoalType.MAXIMIZE) ? -fx : fx, count);
            return x;
        }
        ++count;
    }
    throw new MaxIterationsExceededException(maximalIterationCount);
}