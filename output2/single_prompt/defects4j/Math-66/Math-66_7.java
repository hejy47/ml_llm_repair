## Fixed Function 1
public BrentOptimizer() {
    setMaxEvaluations(Integer.MAX_VALUE);
    setMaximalIterationCount(100);
    setAbsoluteAccuracy(1E-10);
    setRelativeAccuracy(1.0e-14);
}

## Fixed Function 2
protected double doOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
    throw new UnsupportedOperationException();
}

## Fixed Function 3
public double optimize(final UnivariateRealFunction f, final GoalType goalType, final double min, final double max, final double startValue) throws MaxIterationsExceededException, FunctionEvaluationException {
    clearResult();
    return localMin(goalType == GoalType.MINIMIZE, f, goalType, min, startValue, max, getRelativeAccuracy(), getAbsoluteAccuracy());
}

## Fixed Function 4
public double optimize(final UnivariateRealFunction f, final GoalType goalType, final double min, final double max) throws MaxIterationsExceededException, FunctionEvaluationException {
    return optimize(f, goalType, min, max, min + GOLDEN_SECTION * (max - min));
}

## Fixed Function 5
private double localMin(boolean isMinim, UnivariateRealFunction f, GoalType goalType, double lo, double mid, double hi, double eps, double t) throws MaxIterationsExceededException, FunctionEvaluationException {
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
    double v = x;
    double w = x;
    double d = 0;
    double e = 0;
    double fx = f.value(x);
    if (goalType == GoalType.MAXIMIZE) {
        fx = -fx;
    }
    double fv = fx;
    double fw = fx;
    int count = 0;
    while (count < getMaximalIterationCount()) {
        double m = (a + b) / 2.0;
        double tol1 = eps + t * Math.abs(x);
        double tol2 = 2.0 * tol1;
        if (Math.abs(x - m) <= tol2 - 0.5 * (b - a)) {
            setResult(x, (goalType == GoalType.MAXIMIZE) ? -fx : fx, count);
            return x;
        }
        if (Math.abs(e) > tol1) {
            double r = (x - w) * (fx - fv);
            double q = (x - v) * (fx - fw);
            double p = (x - v) * q - (x - w) * r;
            q = 2.0 * (q - r);
            if (q > 0.0) {
                p = -p;
            }
            q = Math.abs(q);
            double etemp = e;
            e = d;
            if (Math.abs(p) >= Math.abs(0.5 * q * etemp) || p <= q * (a - x) || p >= q * (b - x)) {
                d = CGOLD * (e = (x >= m ? a - x : b - x));
            } else {
                d = p / q;
                double u = x + d;
                if (u - a < tol2 || b - u < tol2) {
                    d = x < m ? tol1 : -tol1;
                }
            }
        } else {
            d = CGOLD * (e = (x >= m ? a - x : b - x));
        }
        double u;
        if (Math.abs(d) >= tol1) {
            u = x + d;
        } else {
            u = x + (d > 0 ? tol1 : -tol1);
        }
        double fu = f.value(u);
        if (goalType == GoalType.MAXIMIZE) {
            fu = -fu;
        }
        if (fu <= fx) {
            if (u >= x) {
                a = x;
            } else {
                b = x;
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
            } else if (fu <= fv || v == x || v == w) {
                v = u;
                fv = fu;
            }
        }
        ++count;
    }
    throw new MaxIterationsExceededException(getMaximalIterationCount());
}