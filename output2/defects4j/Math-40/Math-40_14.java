@Override
protected double doSolve() throws TooManyEvaluationsException{
    // prepare arrays with the first points
    final double[] x = new double[maximalOrder + 1];
    final double[] y = new double[maximalOrder + 1];
    x[0] = getMin();
    x[1] = getStartValue();
    x[2] = getMax();
    verifySequence(x[0], x[1], x[2]);
    // evaluate initial guess
    y[1] = computeObjectiveValue(x[1]);
    if (Precision.equals(y[1], 0.0, 1)) {
        // return the initial guess if it is a perfect root.
        return x[1];
    }
    // evaluate first  endpoint
    y[0] = computeObjectiveValue(x[0]);
    if (Precision.equals(y[0], 0.0, 1)) {
        // return the first endpoint if it is a perfect root.
        return x[0];
    }
    int nbPoints;
    int signChangeIndex;
    if (y[0] * y[1] < 0) {
        // reduce interval if it brackets the root
        nbPoints = 2;
        signChangeIndex = 1;
    } else {
        // evaluate second endpoint
        y[2] = computeObjectiveValue(x[2]);
        if (Precision.equals(y[2], 0.0, 1)) {
            // return the second endpoint if it is a perfect root.
            return x[2];
        }
        if (y[1] * y[2] < 0) {
            // use all computed point as a start sampling array for solving
            nbPoints = 3;
            signChangeIndex = 2;
        } else {
            throw new NoBracketingException(x[0], x[2], y[0], y[2]);
        }
    }
    // prepare a work array for inverse polynomial interpolation
    final double[] tmpX = new double[x.length];
    // current tightest bracketing of the root
    double xA = x[signChangeIndex - 1];
    double yA = y[signChangeIndex - 1];
    double absYA = FastMath.abs(yA);
    int agingA = 0;
    double xB = x[signChangeIndex];
    double yB = y[signChangeIndex];
    double absYB = FastMath.abs(yB);
    int agingB = 0;
    // search loop
    while (true) {
        // check convergence of bracketing interval
        final double xTol = getAbsoluteAccuracy() + getRelativeAccuracy() * FastMath.max(FastMath.abs(xA), FastMath.abs(xB));
        if (((xB - xA) <= xTol) || (FastMath.max(absYA, absYB) < getFunctionValueAccuracy())) {
            switch(allowed) {
                case ANY_SIDE:
                    return absYA < absYB ? xA : xB;
                case LEFT_SIDE:
                    return xA;
                case RIGHT_SIDE:
                    return xB;
                case BELOW_SIDE:
                    return (yA <= 0) ? xA : xB;
                case ABOVE_SIDE:
                    return (yA < 0) ? xB : xA;
                default:
                    // this should never happen
                    throw new MathInternalError(null);
            }
        }
        if (nbPoints > getMaximalOrder()) {
            throw new TooManyEvaluationsException(maximalOrder);
        }
        // target for the next evaluation point
        double targetY;
        if (agingA >= MAXIMAL_AGING) {
            // we keep updating the high bracket, try to compensate this
            targetY = -REDUCTION_FACTOR * yB;
        } else if (agingB >= MAXIMAL_AGING) {
            // we keep updating the low bracket, try to compensate this
            targetY = -REDUCTION_FACTOR * yA;
        } else {
            // bracketing is balanced, try to find the root itself
            targetY = 0;
        }
        // make a few attempts to guess a root,
        double nextX;
        int start = 0;
        int end = nbPoints;
        boolean bisect = true;
        while (true) {
            // guess a value for current target, using inverse polynomial interpolation
            System.arraycopy(x, start, tmpX, start, end - start);
            try {
                nextX = UnivariateSolverUtils.solve( new LaguerreSolver(), getFunctionValueAccuracy(), tmpX, y, start, end);
            } catch (NoBracketingException ex) {
                bisect = true;
                break;
            }
            if ((nextX > xA) && (nextX < xB)) {
                break;
            }
            // the guessed root is not strictly
            // inside of the tightest bracketing interval
            if (signChangeIndex - start >= end - signChangeIndex) {
                // we have more points before the sign change, drop the lowest point
                ++start;
            } else {
                // we have more points after sign change, drop the highest point
                --end;
            }
            if (end - start < 2) {
                // we need to do one more attempt
                bisect = true;
                break;
            }
        }
        if (bisect) {
            // fall back to bisection
            nextX = xA + 0.5 * (xB - xA);
            start = signChangeIndex - 1;
            end = signChangeIndex;
        }
        // evaluate the function at the guessed root
        final double nextY = computeObjectiveValue(nextX);
        if (Precision.equals(nextY, 0.0, 1)) {
            // we have found an exact root, since it is not an approximation
            // we don't need to bother about the allowed solutions setting
            return nextX;
        }
        if ((nbPoints > 2) && (end - start != nbPoints)) {
            // we have been forced to ignore some points to keep bracketing,
            // they are probably too far from the root, drop them from now on
            nbPoints = end - start;
            System.arraycopy(x, start, x, 0, nbPoints);
            System.arraycopy(y, start, y, 0, nbPoints);
            signChangeIndex -= start;
        } else if (nbPoints == x.length) {
            // we have to drop one point in order to insert the new one
            nbPoints--;
            // keep the tightest bracketing interval as centered as possible
            if (signChangeIndex >= (x.length + 1) / 2) {
                // we drop the lowest point, we have to shift the arrays and the index
                System.arraycopy(x, 1, x, 0, nbPoints);
                System.arraycopy(y, 1, y, 0, nbPoints);
                --signChangeIndex;
            }
        }
        // insert the last computed point
        //(by construction, we know it lies inside the tightest bracketing interval)
        System.arraycopy(x, signChangeIndex, x, signChangeIndex + 1, nbPoints - signChangeIndex);
        x[signChangeIndex] = nextX;
        System.arraycopy(y, signChangeIndex, y, signChangeIndex + 1, nbPoints - signChangeIndex);
        y[signChangeIndex] = nextY;
        ++nbPoints;
        // update the bracketing interval
        if (nextY * yA <= 0) {
            // the sign change occurs before the inserted point
            xB = nextX;
            yB = nextY;
            absYB = FastMath.abs(yB);
            ++agingA;
            agingB = 0;
        } else {
            // the sign change occurs after the inserted point
            xA = nextX;
            yA = nextY;
            absYA = FastMath.abs(yA);
            agingA = 0;
            ++agingB;
            // update the sign change index
            signChangeIndex++;
        }
    }
}