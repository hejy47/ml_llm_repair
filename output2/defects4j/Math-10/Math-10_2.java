public void atan2(final double[] y, final int yOffset, final double[] x, final int xOffset, final double[] result, final int resultOffset) {
    // compute r = sqrt(x^2+y^2)
    double[] tmp1 = new double[getSize()];
    // x^2
    multiply(x, xOffset, x, xOffset, tmp1, 0);
    double[] tmp2 = new double[getSize()];
    // y^2
    multiply(y, yOffset, y, yOffset, tmp2, 0);
    // x^2 + y^2
    add(tmp1, 0, tmp2, 0, tmp2, 0);
    // r = sqrt(x^2 + y^2)
    rootN(tmp2, 0, 2, tmp1, 0);
    if (x[xOffset] >= 0) {
        // compute atan2(y, x) = 2 atan(y / (r + x))
        // r + x
        add(tmp1, 0, x, xOffset, tmp2, 0);
        // y /(r + x)
        divide(y, yOffset, tmp2, 0, tmp1, 0);
        // atan(y / (r + x))
        atan(tmp1, 0, tmp2, 0);
        for (int i = 0; i < tmp2.length; ++i) {
            // 2 * atan(y / (r + x))
            result[resultOffset + i] = 2 * tmp2[i];
        }
        // fix value to take special cases (+0/+0, +0/-0, -0/+0, -0/-0, +/-infinity) correctly
        if (Double.isNaN(result[resultOffset])) {
            result[resultOffset] = FastMath.atan2(y[yOffset], x[xOffset]);
        }
    } else {
        // compute atan2(y, x) = +/- pi - 2 atan(y / (r - x))
        // r - x
        subtract(tmp1, 0, x, xOffset, tmp2, 0);
        // y /(r - x)
        divide(y, yOffset, tmp2, 0, tmp1, 0);
        // atan(y / (r - x))
        atan(tmp1, 0, tmp2, 0);
        double atanRes = -2 * tmp2[0];
        if (Double.isNaN(atanRes)) {
            atanRes = FastMath.atan2(y[yOffset], x[xOffset]);
            result[resultOffset] = (Double.isNaN(atanRes) ? atanRes : ((atanRes > 0) ? (atanRes - FastMath.PI) : (atanRes + FastMath.PI)));
        } else {
            result[resultOffset] = ((tmp2[0] <= 0) ? -FastMath.PI : FastMath.PI) - atanRes;
        }
        for (int i = 1; i < tmp2.length; ++i) {
            // +/-pi - 2 * atan(y / (r - x))
            result[resultOffset + i] = -2 * tmp2[i];
        }
    }
}