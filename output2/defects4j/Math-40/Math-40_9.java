@Override
protected double doSolve() {
    final double current = getStartValue();
    double y0 = computeObjectiveValue(current);
    if (Precision.equals(y0, 0.0, 1)) {
        return current;
    }
    final double step = FastMath.max(FastMath.abs(current), getAbsoluteAccuracy());
    double x1 = current + step;
    double y1 = computeObjectiveValue(x1);
    if (Precision.equals(y1, 0.0, 1)) {
        return x1;
    }
    if (y0 * y1 < 0) {
        // keep the current point as the best candidate for root
        double x0 = current;
        double y2;
        int i = 0;  // iteration counter
        while (true) {
            ++i;
            double x2;
            if (i > MAX_EVAL) {
                throw new TooManyEvaluationsException();
            }
            x2 = x0 + (x1 - x0) * y0 / (y0 - y1);
            y2 = computeObjectiveValue(x2);
            if (Precision.equals(y2, 0.0, 1)) {
                return x2;
            }
            // update the bounds
            if (y0 * y2 < 0) {
                x1 = x2;
                y1 = y2;
            } else {
                x0 = x2;
                y0 = y2;
            }
            // check for convergence
            final double xTol = getAbsoluteAccuracy() + getRelativeAccuracy() * FastMath.max(FastMath.abs(x0), FastMath.abs(x1));
            if ((FastMath.abs(x1 - x0) < xTol) && (FastMath.abs(y2) < getFunctionValueAccuracy())) {
                return x2;
            }
        }
    }
    throw new NoBracketingException(current - step, current + step, y0, y1);
}