## Fixed Function 1
public BrentOptimizer() {
    setMaxEvaluations(Integer.MAX_VALUE);
    setMaximalIterationCount(100);
    setAbsoluteAccuracy(1E-10);
    setRelativeAccuracy(1.0e-14);
}

## Fixed Function 2
protected double doOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
    throw new MathUnsupportedOperationException();
}

## Fixed Function 3
public double optimize(final UnivariateRealFunction f, final GoalType goalType, final double min, final double max, final double startValue) throws MaxIterationsExceededException, FunctionEvaluationException {
    clearResult();
    return localMin(getGoalType() == GoalType.MINIMIZE, f, goalType, min, startValue, max, getRelativeAccuracy(), getAbsoluteAccuracy());
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
    final double golden = 0.5*(3.0 - Math.sqrt(5.0));
    double a, b, d = 0.0, e = 0.0, etemp, fu, fv, fw, fx, p, q, r, tol1, tol2, u, v, w, x, xm;
    x = mid;
    a = (lo < hi ? lo : hi);
    b = (lo > hi ? lo : hi);
    e = 0.0;
    v = x;
    w = x;
    fx = computeObjectiveValue(f, x);
    if (goalType == GoalType.MAXIMIZE) {
        fx = -fx;
    }
    fv = fx;
    fw = fx;
    tol1 = eps*Math.abs(x) + t;
    tol2 = 2.0*tol1;
    while (true) {
        xm = 0.5*(a + b);
        tol1 = eps*Math.abs(x) + t;
        tol2 = 2.0*tol1;
        if (Math.abs(x - xm) <= (tol2 - 0.5*(b - a))) {
            setResult(x, (goalType == GoalType.MAXIMIZE) ? -fx : fx, getEvaluations());
            return x;
        }
        if (Math.abs(e) > tol1) {
            r = (x - w)*(fx - fv);
            q = (x - v)*(fx - fw);
            p = (x - v)*q - (x - w)*r;
            q = 2.0*(q - r);
            if (q > 0.0) {
                p = -p;
            }
            q = Math.abs(q);
            etemp = e;
            e = d;
            if ((Math.abs(p) >= Math.abs(0.5*q*etemp)) || (p <= q*(a - x)) || (p >= q*(b - x))) {
                // Fall back to golden section step.
                if (x >= xm) {
                    e = a - x;
                } else {
                    e = b - x;
                }
                d = golden*e;
            } else {
                d = p/q;
                u = x + d;
                if (((u - a) < tol2) || ((b - u) < tol2)) {
                    if (x < xm) {
                        d = tol1;
                    } else {
                        d = -tol1;
                    }
                }
            }
        } else {
            // Golden section step.
            if (x >= xm) {
                e = a - x;
            } else {
                e = b - x;
            }
            d = golden*e;
        }
        if (Math.abs(d) >= tol1) {
            u = x + d;
        } else {
            if (d > 0) {
                u = x + tol1;
            } else {
                u = x - tol1;
            }
        }
        fu = computeObjectiveValue(f, u);
        if (goalType == GoalType.MAXIMIZE) {
            fu = -fu;
        }
        etemp = (x >= xm ? a - x : b - x);
        if (fu <= fx) {
            if (x >= xm) {
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
            if (x < xm) {
                a = u;
            } else {
                b = u;
            }
            if ((fu <= fw) || (w == x)) {
                v = w;
                fv = fw;
                w = u;
                fw = fu;
            } else if ((fu <= fv) || (v == x) || (v == w)) {
                v = u;
                fv = fu;
            }
        }
    }
}