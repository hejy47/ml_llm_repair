## Fixed Function 1
public BrentOptimizer() {
    setMaxEvaluations(Integer.MAX_VALUE);
    setMaximalIterationCount(100);
    setAbsoluteAccuracy(1E-10);
    setRelativeAccuracy(1.0e-14);
    setResult(new MinMaxValuePair(Double.NaN, Double.NaN), 0, 0);
}

## Fixed Function 2
protected double doOptimize() throws MaxIterationsExceededException, FunctionEvaluationException {
    throw new FunctionEvaluationException();
}

## Fixed Function 3
public double optimize(final UnivariateRealFunction f, final GoalType goalType, final double min, final double max, final double startValue) throws MaxIterationsExceededException, FunctionEvaluationException {
    clearResult();
    return localMin(getGoalType() == goalType.MINIMIZE ? min : max, f, goalType, min, startValue, max);
}

## Fixed Function 4
public double optimize(final UnivariateRealFunction f, final GoalType goalType, final double min, final double max) throws MaxIterationsExceededException, FunctionEvaluationException {
    return optimize(f, goalType, min, max, min + GOLDEN_SECTION * (max - min));
}

## Fixed Function 5
private double localMin(final double min, final UnivariateRealFunction f, final GoalType goalType, double a, double b, double c) throws FunctionEvaluationException, MaxIterationsExceededException {
    double d, e, eps, xm, fa, fb, fc, p, q, r, s, tol1, tol2, u, v, w, x;
    boolean mflag;
    eps = getAbsoluteAccuracy();
    int i, iter = 0;
    a = (a < c) ? a : c;
    b = (b > c) ? b : c;
    v = w = x = c;
    fb = fc = fa = f.value(a);
    if (goalType == GoalType.MINIMIZE ? fa < fb : fa > fb) {
        double tmp = a;
        a = b;
        b = tmp;
        fa = fb;
        fb = f.value(b);
    }
    boolean breakOuterLoop = false;
    for (; iter < maximalIterationCount; iter++) {
        xm = 0.5 * (a + b);
        tol1 = eps * Math.abs(x) + getRelativeAccuracy();
        tol2 = 2.0 * tol1;
        if (Math.abs(x - xm) <= (tol2 - 0.5 * (b - a))) {
            setResult(new MinMaxValuePair(goalType == GoalType.MINIMIZE ? x : v, goalType == GoalType.MINIMIZE ? v : x), iter, 0);
            return x;
        }
        if (Math.abs(e) > tol1) {
            mflag = false;
            s = (x - w) * (fx - fv);
            q = (x - v) * (fx - fw);
            p = (x - v) * q - (x - w) * s;
            q = 2.0 * (q - s);
            if (q > 0.0) {
                p = -p;
            }
            q = Math.abs(q);
            double etemp = e;
            e = d;
            if (Math.abs(p) >= Math.abs(0.5 * q * etemp) || p <= q * (a - x) || p >= q * (b - x)) {
                d = CGOLD * (e = (x >= xm ? a - x : b - x));
                mflag = true;
            } else {
                d = p / q;
                u = x + d;
                if (u - a < tol2 || b - u < tol2) {
                    d = xm - x >= 0 ? tol1 : -tol1;
                    mflag = true;
                }
            }
            if (!mflag) {
                u = x + d;
                fu = f.value(u);
                if (goalType == GoalType.MINIMIZE ? fu < fx : fu > fx) {
                    if (u >= x) {
                        a = x;
                    } else {
                        b = x;
                    }
                    v = w;
                    w = x;
                    x = u;
                    fv = fw;
                    fw = fx;
                    fx = fu;
                } else {
                    if (u < x) {
                        a = u;
                    } else {
                        b = u;
                    }
                    if (goalType == GoalType.MINIMIZE ? fu < fw : fu > fw || w == x) {
                        v = w;
                        w = u;
                        fv = fw;
                        fw = fu;
                    } else if (goalType == GoalType.MINIMIZE ? fu < fv : fu > fv || v == x || v == w) {
                        v = u;
                        fv = fu;
                    }
                }
            }
        } else {
            d = CGOLD * (e = (x >= xm ? a - x : b - x));
            mflag = true;
        }
        if (breakOuterLoop) {
            break;
        }
        if (Math.abs(d) >= tol1) {
            u = x + d;
            fu = f.value(u);
            ++i;
            if (goalType == GoalType.MINIMIZE ? fu < fx : fu > fx) {
                if (u >= x) {
                    a = x;
                } else {
                    b = x;
                }
                v = w;
                w = x;
                x = u;
                fv = fw;
                fw = fx;
                fx = fu;
            } else {
                if (u < x) {
                    a = u;
                } else {
                    b = u;
                }
                if (goalType == GoalType.MINIMIZE ? fu < fw : fu > fw || w == x) {
                    v = w;
                    w = u;
                    fv = fw;
                    fw = fu;
                } else if (goalType == GoalType.MINIMIZE ? fu < fv : fu > fv || v == x || v == w) {
                    v = u;
                    fv = fu;
                }
            }
        } else {
            if (x < xm) {
                e = b - x;
            } else {
                e = a - x;
            }
            d = CGOLD * e;
            mflag = true;
        }
        // Swap the roles of a, b, and c to avoid recomputing f(b) and f(c).
        if (goalType == GoalType.MINIMIZE ? fa < fb : fa > fb) {
            double tmp = a;
            a = b;
            b = tmp;
            fa = fb;
            fb = f.value(b);
        }
        setResult(new MinMaxValuePair(goalType == GoalType.MINIMIZE ? x : v, goalType == GoalType.MINIMIZE ? v : x), iter, i * 3);
    }
    throw new MaxIterationsExceededException(maximalIterationCount);
}