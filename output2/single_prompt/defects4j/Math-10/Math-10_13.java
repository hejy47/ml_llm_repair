public void atan2(final double[] y, final int yOffset, final double[] x, final int xOffset, final double[] result, final int resultOffset) {
    double EPSILON = 1E-10; // declare EPSILON to avoid division by zero

    // compute r = sqrt(x^2+y^2)
    double[] tmp1 = new double[getSize()];
    multiply(x, xOffset, x, xOffset, tmp1, 0);
    double[] tmp2 = new double[getSize()];
    multiply(y, yOffset, y, yOffset, tmp2, 0);
    add(tmp1, 0, tmp2, 0, tmp2, 0);
    rootN(tmp2, 0, 2, tmp1, 0);

    if (Math.abs(x[xOffset]) < EPSILON) {
        if (y[yOffset] > 0) {
            result[resultOffset] = FastMath.PI / 2.0;
        } else if (y[yOffset] < 0) {
            result[resultOffset] = -FastMath.PI / 2.0;
        } else {
            result[resultOffset] = 0.0; // to handle +/-0 or NaN inputs
        }
    } else if (x[xOffset] > 0) {
        double r_plus_x = tmp2[0] + x[xOffset];
        divide(y, yOffset, r_plus_x, tmp1, 0);
        atan(tmp1, 0, tmp2, 0);
        result[resultOffset] = 2 * tmp2[0];

        for (int i = 1; i < tmp2.length; ++i) {
            result[resultOffset + i] = 2 * tmp2[i];
        }
    } else {
        double r_minus_x = tmp2[0] - x[xOffset];
        divide(y, yOffset, r_minus_x, tmp1, 0);
        atan(tmp1, 0, tmp2, 0);
        result[resultOffset] = ((y[yOffset] > 0) ? FastMath.PI : -FastMath.PI) - 2 * tmp2[0];

        for (int i = 1; i < tmp2.length; i++) {
            result[resultOffset + i] = -2 * tmp2[i];
        }
    }
}