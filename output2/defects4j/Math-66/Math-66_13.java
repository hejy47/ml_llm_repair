## Fixed Function 1
public BrentOptimizer() {
    setMaxEvaluations(Integer.MAX_VALUE);
    setMaximalIterationCount(100);
    setAbsoluteAccuracy(1E-10);
    setRelativeAccuracy(1.0e-14);
    setFunctionValueAccuracy(1.0e-14);
}

## Fixed Function 2
protected double doOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
    throw new FunctionEvaluationException(new UnsupportedOperationException());
}

## Fixed Function 3
public double optimize(final UnivariateRealFunction f, final GoalType goalType, final double min, final double max, final double startValue) throws MaxIterationsExceededException, FunctionEvaluationException {
    clearResult();
    return localMin(getGoalType() == GoalType.MINIMIZE, f, goalType, min, startValue, max, getRelativeAccuracy(), getAbsoluteAccuracy(), getFunctionValueAccuracy());
}

## Fixed Function 4
public double optimize(final UnivariateRealFunction f, final GoalType goalType, final double min, final double max) throws MaxIterationsExceededException, FunctionEvaluationException {
    return optimize(f, goalType, min, max, min + GOLDEN_SECTION * (max - min));
}

## Fixed Function 5
private double localMin(boolean isMinim, UnivariateRealFunction f, GoalType goalType, double lo, double mid, double hi, double eps, double t, double v) throws MaxIterationsExceededException, FunctionEvaluationException {
    if (eps <= 0) {
        throw new NotStrictlyPositiveException(eps);
    }
    if (t <= 0) {
        throw new NotStrictlyPositiveException(t);
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
    double w = x;
    double vfw = computeObjectiveValue(f, v);
    double fx = computeObjectiveValue(f, x);
    double fw = fx;
    double d = 0;
    double e = 0;
    int count = 0;
    while (count < getMaximalIterationCount()) {
        double m = 0.5 * (a + b);
        final double tol1 = eps * Math.abs(x) + t;
        final double tol2 = 2 * tol1;
        // Check stopping criterion.
        if (Math.abs(x - m) > tol2 - 0.5 * (b - a)) {
            double p = 0;
            double q = 0;
            double r = 0;
            double u = 0;
            if (Math.abs(e) > tol1) {
                // Fit parabola.
                r = (x - w) * (fx - vfw);
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
                if (p > q * (a - x) && p < q * (b - x) && Math.abs(p) < Math.abs(0.5 * q * r)) {
                    // Parabolic interpolation step.
                    d = p / q;
                    u = x + d;
                    // f must not be evaluated too close to a or b.
                    if (u - a < tol2 || b - u < tol2) {
                        if (x <= m) {
                            d = tol1;
                        } else {
                            d = -tol1;
                        }
                    }
                } else {
                    // Golden section step.
                    if (x < m) {
                        e = b - x;
                    } else {
                        e = a - x;
                    }
                    d = GOLDEN_SECTION * e;
                }
            } else {
                // Golden section step.
                if (x < m) {
                    e = b - x;
                } else {
                    e = a - x;
                }
                d = GOLDEN_SECTION * e;
            }
            // Update by at least "tol1".
            if (Math.abs(d) < tol1) {
                if (d >= 0) {
                    u = x + tol1;
                } else {
                    u = x - tol1;
                }
            } else {
                u = x + d;
            }
            double fu = computeObjectiveValue(f, u);
            // Update a, b, v, w and x.
            if (goalType == GoalType.MINIMIZE) {
                if (fu <= fx) {
                    if (u < x) {
                        b = x;
                    } else {
                        a = x;
                    }
                    v = w;
                    vfw = fw;
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
                        vfw = fw;
                        w = u;
                        fw = fu;
                    } else if (fu <= vfw || v == x || v == w) {
                        v = u;
                        vfw = fu;
                    }
                }
            } else {
                if (fu >= fx) {
                    if (u < x) {
                        b = x;
                    } else {
                        a = x;
                    }
                    v = w;
                    vfw = fw;
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
                    if (fu >= fw || w == x) {
                        v = w;
                        vfw = fw;
                        w = u;
                        fw = fu;
                    } else if (fu >= vfw || v == x || v == w) {
                        v = u;
                        vfw = fu;
                    }
                }
            }
        } else {
            // termination
            setResult(x, fx, count);
            return x;
        }
        ++count;
    }
    throw new MaxIterationsExceededException(getMaximalIterationCount());
}