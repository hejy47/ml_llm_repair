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
    } else {
        // compute atan2(y, x) = +/- pi - 2 atan(y / (r - x))
        // r - x
        subtract(tmp1, 0, x, xOffset, tmp2, 0);
        final double[] tmp3 = new double[getSize()];
        if (tmp2[0] == 0.0) {
            tmp2[0] = FastMath.ulp(tmp2[0]);
        }
        // y /(r - x)
        divide(y, yOffset, tmp2, 0, tmp1, 0);
        // atan(y / (r - x))
        atan(tmp1, 0, tmp3, 0);
        boolean negate = (tmp2[0] < 0);
        if (getSize() > 1) {
            final long m = getMantissa(tmp2);
            final int e = getExponent(tmp2);
            if (e > -1023 && m >= 0x10000000000000L) {
                // adjust the value to the representable range
                if (negate) {
                    tmp2[getSize() - 1] = -0x1.0p970;
                } else {
                    tmp2[getSize() - 1] = 0x1.0p970;
                }
                Arrays.fill(tmp2, 0, getSize() - 1, 0);
                negate = false;
            }
        }
        result[resultOffset] = negate ? -FastMath.PI - 2 * tmp3[0] : FastMath.PI - 2 * tmp3[0];
        for (int i = 1; i < tmp3.length; ++i) {
            // +/-pi - 2 * atan(y / (r - x))
            result[resultOffset + i] = -2 * tmp3[i];
        }
    }
    // fix value to take special cases (+0/+0, +0/-0, -0/+0, -0/-0, +/-infinity) correctly
}