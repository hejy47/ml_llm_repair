public double evaluate(double x, double epsilon, int maxIterations) {
    final double small = 1e-50;
    double hPrev = getA(0, x);
    // use the value of small as epsilon criteria for zero checks
    if (Precision.equals(hPrev, 0.0, small)) {
        hPrev = small;
    }
    int n = 1;
    double dPrev = 0.0;
    double pPrev = 1.0;
    double qPrev = 0.0;
    double cPrev = hPrev;
    double hN = hPrev;
    while (n < maxIterations) {
        final double a = getA(n, x);
        final double b = getB(n, x);
        double cN = a * hPrev + b * pPrev;
        double qN = a * qPrev + b * dPrev;
        if (Double.isInfinite(cN) || Double.isInfinite(qN)) {
            double scaleFactor = 1d;
            double lastScaleFactor = 1d;
            final int maxPower = 5;
            final double scale = FastMath.max(a, b);
            if (scale <= 0) {
                // Can't scale
                throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, x);
            }
            for (int i = 0; i < maxPower; i++) {
                lastScaleFactor = scaleFactor;
                scaleFactor *= scale;
                if (a != 0.0 && a > b) {
                    cN = hPrev / lastScaleFactor + (b / scaleFactor * pPrev);
                    qN = qPrev / lastScaleFactor + (b / scaleFactor * dPrev);
                } else if (b != 0) {
                    cN = (a / scaleFactor * hPrev) + pPrev / lastScaleFactor;
                    qN = (a / scaleFactor * qPrev) + dPrev / lastScaleFactor;
                }
                if (!(Double.isInfinite(cN) || Double.isInfinite(qN))) {
                    break;
                }
            }
            if (Double.isInfinite(cN) || Double.isInfinite(qN)) {
                throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, x);
            }
        }
        final double deltaN = cN / qN / cPrev;
        hN = cPrev * deltaN;
        if (Double.isInfinite(hN)) {
            throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, x);
        }
        if (Double.isNaN(hN)) {
            throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_NAN_DIVERGENCE, x);
        }
        if (FastMath.abs(deltaN - 1.0) < epsilon) {
            break;
        }
        dPrev = qPrev;
        qPrev = qN;
        pPrev = pPrev * deltaN;
        cPrev = cN / qN;
        hPrev = cN;
        n++;
    }
    if (n >= maxIterations) {
        throw new MaxCountExceededException(LocalizedFormats.NON_CONVERGENT_CONTINUED_FRACTION, maxIterations, x);
    }
    return hN;
}