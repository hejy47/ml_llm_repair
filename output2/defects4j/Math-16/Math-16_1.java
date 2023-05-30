## Fixed Function 1
public static double cosh(double x) {
    if (Double.isNaN(x)) {
        return Double.NaN;
    }
    // cosh[z] = (exp(z) + exp(-z))/2
    // for numbers with magnitude 20 or so,
    // exp(-z) can be ignored in comparison with exp(z)
    if (x > 20) {
        // Avoid overflow (MATH-905).
        return 0.5 * Math.exp(x);
    }
    if (x < -20) {
        // Avoid overflow (MATH-905).
        return 0.5 * Math.exp(-x);
    }
    final double[] hiPrec = new double[2];
    if (x < 0.0) {
        x = -x;
    }
    Math.exp(x, 0.0, hiPrec);
    double ya = hiPrec[0] + hiPrec[1];
    double yb = -(ya - hiPrec[0] - hiPrec[1]);
    double temp = ya * HEX_40000000;
    double yaa = ya + temp - temp;
    double yab = ya - yaa;
    // recip = 1/y
    double recip = 1.0 / ya;
    temp = recip * HEX_40000000;
    double recipa = recip + temp - temp;
    double recipb = recip - recipa;
    // Correct for rounding in division
    recipb += (1.0 - yaa * recipa - yaa * recipb - yab * recipa - yab * recipb) * recip;
    // Account for yb in approximation to true value:
    // cosh(x) ~ (y + (y*y-1)/(2y)) for y=exp(x)
    double y1 = ya + yb;
    double y2 = yb - (y1 - ya);
    yb = -(y1 * y2 - 1.0) / (2.0 * y1);
    temp = yb * HEX_40000000;
    double yba = yb + temp - temp;
    double ybb = yb - yba;
    temp = y1 * HEX_40000000;
    yaa = y1 + temp - temp;
    yab = y1 - yaa;
    yb += (-(yaa * yba - ybb * yab) / (yaa * yaa + 2.0 * yaa * yb));
    double result = ya + yb;
    result *= 0.5;
    return result;
}

## Fixed Function 2
public static double sinh(double x) {
    if (Double.isNaN(x)) {
        return Double.NaN;
    }
    boolean negate = false;
    // sinh[z] = (exp(z) - exp(-z))/2
    // for values of z larger than about 20,
    // exp(-z) can be ignored in comparison with exp(z)
    if (x > 20) {
        // Avoid overflow (MATH-905).
        return 0.5 * Math.exp(x);
    }
    if (x < -20) {
        // Avoid overflow (MATH-905).
        return -0.5 * Math.exp(-x);
    }
    if (x == 0) {
        return x;
    }
    if (x < 0.0) {
        x = -x;
        negate = true;
    }
    double result;
    if (x > 0.25) {
        double[] hiPrec = new double[2];
        Math.exp(x, 0.0, hiPrec);
        double ya = hiPrec[0] + hiPrec[1];
        double yb = -(ya - hiPrec[0] - hiPrec[1]);
        double denom = 1.0 / (ya + 1.0);
        double denomr = Math.rint(denom * HEX_40000000) / HEX_40000000;
        double denomb = denom - denomr;
        double ratio = ya * denom;
        double temp = ratio * HEX_40000000;
        double ra = ratio + temp - temp;
        double rb = ratio - ra;
        temp = denom * HEX_40000000;
        double za = denom + temp - temp;
        double zb = denom - za;
        rb += (ya - za * ra - za * rb - zb * ra - zb * rb) * denom;
        // Adjust for yb
        rb += yb * denom;
        rb += ya * denomb * denomr;
        double temp1 = ya + ra;
        double temp2 = temp1 + rb;
        yb += -(temp1 - ya - ra);
        ya = temp1;
        yb += -(temp2 - ya - rb);
        ya = temp2;
        result = ya + yb;
        result *= 0.5;
    } else {
        double[] hiPrec = new double[2];
        Math.expm1(x, hiPrec);
        double ya = hiPrec[0] + hiPrec[1];
        double yb = -(ya - hiPrec[0] - hiPrec[1]);
        double denom = 1.0 / (ya + 1.0);
        double denomr = Math.rint(denom * HEX_40000000) / HEX_40000000;
        double denomb = denom - denomr;
        double ratio = ya * denom;
        double temp = ratio * HEX_40000000;
        double ra = ratio + temp - temp;
        double rb = ratio - ra;
        temp = denom * HEX_40000000;
        double za = denom + temp - temp;
        double zb = denom - za;
        rb += (ya - za * ra - za * rb - zb * ra - zb * rb) * denom;
        // Adjust for yb
        rb += yb * denom;
        rb += ya * denomb * denomr;
        double temp1 = ya + ra;
        double temp2 = temp1 + rb;
        yb += -(temp1 - ya - ra);
        ya = temp1;
        yb += -(temp2 - ya - rb);
        ya = temp2;
        result = ya + yb;
        result *= 0.5;
    }
    if (negate) {
        result = -result;
    }
    return result;
}