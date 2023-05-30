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
    boolean negate = false;
    if (x < 0.0) {
        x = -x;
        negate = true;
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
    return negate ? -result : result;
}

## Fixed Function 2
public static double sinh(double x) {
    if (Double.isNaN(x)) {
        return Double.NaN;
    }
    boolean negate = false;
    // sinh[z] = (exp(z) - exp(-z) / 2
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
    double result;
    if (x < 0) {
        x = -x;
        negate = true;
    }

    if (x > 0.25) {
        // Use exp(x) - exp(-x) instead of (exp(x) - exp(-x)) / 2 to avoid cancellation issues
        // sinh(x) = (exp(x) - exp(-x)) / 2
        // Coth(x) = (exp(x) + exp(-x)) / (exp(x) - exp(-x)) = 1 / Tanh(x)
        // Therefore, Tanh(x) = (exp(x) - exp(-x)) / (exp(x) + exp(-x))
        // Note: This uses high precision calculation for exp, ln and division
        double[] hiPrec = new double[2];
        Math.exp(x, 0.0, hiPrec);
        double ya = hiPrec[0] + hiPrec[1];
        Math.exp(-x, 0.0, hiPrec);
        double yb = -(hiPrec[0] + hiPrec[1] - 1.0);
        double na = ya - yb;
        double nb = yb - (na - ya);
        yb = 1d / ya;
        double temp = yb * HEX_40000000;
        double yba = yb + temp - temp;
        double ybb = yb - yba;
        // Correct for rounding in division
        ybb += (1.0 - na * yba - na * ybb - nb * yba - nb * ybb) * yb;
        // Account for nb
        ybb += nb * yb * yb;

        // y = y + 1/y
        temp = yb + yba;
        ybb += -(temp - yb - yba);
        yb = temp;
        temp = yb + ybb;
        ybb += -(temp - yb - ybb);
        yb = temp;

        result = -na - nb;
        temp = result + yb;
        ybb += -(temp - result - yb);
        result = temp;
        result += ybb;
        // Result is -expm1(-2x)/(expm1(-2x)+2)
        result = (result + 1d) / (ya + result);
        result = -(result + ya);

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
        temp = ya + ra;
        yb += -(temp - ya - ra);
        ya = temp;
        temp = ya + rb;
        yb += -(temp - ya - rb);
        ya = temp;
        result = ya + yb;
    }
    return negate ? -result : result;
}