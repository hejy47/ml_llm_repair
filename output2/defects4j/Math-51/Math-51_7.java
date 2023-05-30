protected final double doSolve() {
    // Get initial solution
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    double f1 = computeObjectiveValue(x1);
    // If one of the bounds is the exact root, return it. Since these are
    // not under-approximations or over-approximations, we can return them
    // regardless of the allowed solutions.
    if (f0 == 0.0) {
        return x0;
    }
    if (f1 == 0.0) {
        return x1;
    }
    // Verify bracketing of initial solution.
    verifyBracketing(x0, x1);
    // Get accuracies.
    final double ftol = getFunctionValueAccuracy();
    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();
    // Keep track of inverted intervals, meaning that the left bound is
    // larger than the right bound.
    boolean inverted = false;
    // Keep finding better approximations.
    while (true) {
        // Calculate the next approximation.
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);
        // Handle too small function values.
        if (FastMath.abs(fx) <= ftol) {
            switch (allowed) {
                case ANY_SIDE:
                    return x;
                case LEFT_SIDE:
                    if (inverted) {
                        return x;
                    }
                    break;
                case RIGHT_SIDE:
                    if (!inverted) {
                        return x;
                    }
                    break;
                case BELOW_SIDE:
                    if (fx <= 0) {
                        return x;
                    }
                    break;
                case ABOVE_SIDE:
                    if (fx >= 0) {
                        return x;
                    }
                    break;
                default:
                    throw new MathInternalError();
            }
        }
        // Update the bounds with the new approximation.
        if (f1 * fx < 0) {
            // The value of x1 has switched to the other bound, thus inverting
            // the interval.
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else {
            switch (method) {
                case ILLINOIS:
                    if (f0 * fx < 0) {
                        f1 *= 0.5;
                    } else {
                        f0 = f1;
                        f1 *= 0.5;
                    }
                    break;
                case PEGASUS:
                    if (f0 * fx < 0) {
                        final double q = f0 / f1;
                        final double t = f0 / (f0 + f1);
                        f1 = fx;
                        f0 *= t;
                        x1 = x;
                        x0 += q * (x - x0);
                    } else {
                        final double q = f1 / f0;
                        final double t = f1 / (f0 + f1);
                        f0 = fx;
                        f1 *= t;
                        x0 = x;
                        x1 += q * (x - x1);
                    }
                    break;
                default:
                    // Update formula cannot make any progress: Update the
                    // search interval.
                    x0 = x;
                    f0 = fx;
                    f1 *= 0.5;
                    break;
            }
        }
        // If the current interval is within the given accuracies, we
        // are satisfied with the current approximation.
        if (FastMath.abs(x1 - x0) <= FastMath.max(rtol * FastMath.max(FastMath.abs(x1), FastMath.abs(x0)), atol)) {
            switch (allowed) {
                case ANY_SIDE:
                    return x;
                case LEFT_SIDE:
                    return inverted ? x : x0;
                case RIGHT_SIDE:
                    return inverted ? x1 : x;
                case BELOW_SIDE:
                    return (fx <= 0) ? x : x0;
                case ABOVE_SIDE:
                    return (fx >= 0) ? x : x0;
                default:
                    throw new MathInternalError();
            }
        }
        x1 = x;
        f1 = fx;
    }
}