## Fixed Function 1
public static double cosh(double x) {
    if (x != x) {
        return x;
    }
    // cosh[z] = (exp(z) + exp(-z))/2
    // for numbers with magnitude 20 or so,
    // exp(-z) can be ignored in comparison with exp(z)
    if (x > 20) {
        // Avoid overflow (MATH-905).
        return 0.5 * exp(x);
    }
    if (x < -20) {
        // Avoid overflow (MATH-905).
        return 0.5 * exp(-x);
    }
    final double[] hiPrec = new double[2];
    if (x < 0.0) {
        x = -x;
    }
    exp(x, 0.0, hiPrec);
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
    // Account for yb
    recipb += -yb * recip * recip;
    // y = y + 1/y
    temp = ya + recipa;
    yb += -(temp - ya - recipa);
    ya = temp;
    temp = ya + recipb;
    yb += -(temp - ya - recipb);
    ya = temp;
    double result = ya + yb;
    result *= 0.5;
    if (Double.isNaN(result)) {
        throw new ArithmeticException("cosh() overflow: " + x);
    }
    return result;
}

## Fixed Function 2
public static double sinh(double x) {
    boolean negate = false;
    if (x != x) {
        return x;
    }
    // sinh[z] = (exp(z) - exp(-z) / 2
    // for values of z larger than about 20,
    // exp(-z) can be ignored in comparison with exp(z)
    if (x > 20) {
        // Avoid overflow (MATH-905).
        return 0.5 * exp(x);
    }
    if (x < -20) {
        // Avoid overflow (MATH-905).
        return -0.5 * exp(-x);
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
        exp(x, 0.0, hiPrec);
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
        // Account for yb
        recipb += -yb * recip * recip;
        recipa = -recipa;
        recipb = -recipb;
        // y = y + 1/y
        temp = ya + recipa;
        yb += -(temp - ya - recipa);
        ya = temp;
        temp = ya + recipb;
        yb += -(temp - ya - recipb);
        ya = temp;
        result = ya + yb;
        result *= 0.5;
        if (Double.isNaN(result)) {
            throw new ArithmeticException("sinh() overflow: " + x);
        }
    } else {
        double[] hiPrec = new double[2];
        expm1(x, hiPrec);
        double ya = hiPrec[0] + hiPrec[1];
        double yb = -(ya - hiPrec[0] - hiPrec[1]);
        double denom = 1.0 + ya;
        double denomr = 1.0 / denom;
        double denomb = -(denom - 1.0 - ya) + yb;
        double ratio = ya * denomr;
        double temp = ratio * HEX_40000000;
        double ra = ratio + temp - temp;
        double rb = ratio - ra;
        temp = denom * HEX_40000000;
        double za = denom + temp - temp;
        double zb = denom - za;
        rb += (ya - za * ra - za * rb - zb * ra - zb * rb) * denomr;
        // Adjust for yb
        // numerator
        rb += yb * denomr;
        // denominator
        rb += -ya * denomb * denomr * denomr;
        // y = y - 1/y
        temp = ya + ra;
        yb += -(temp - ya - ra);
        ya = temp;
        temp = ya + rb;
        yb += -(temp - ya - rb);
        ya = temp;
        result = ya + yb;
        result *= 0.5;
        if (Double.isNaN(result)) {
            throw new ArithmeticException("sinh() overflow: " + x);
        }
    }
    if (negate) {
        result = -result;
    }
    return result;
}