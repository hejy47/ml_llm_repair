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
        return 0.5 * FastMath.exp(x);
    }
    if (x < -20) {
        // Avoid overflow (MATH-905).
        return 0.5 * FastMath.exp(-x);
    }
    final double[] hiPrec = new double[2];
    double absX = FastMath.abs(x);
    FastMath.exp(absX, 0.0, hiPrec);
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
    return FastMath.copySign(result, x);
}

## Fixed Function 2
public static double sinh(double x) {
    if (Double.isNaN(x) || Double.isInfinite(x)) {
        return x;
    }
    boolean negate = false;
    if (x == 0.0) {
        return x;
    }
    if (x < 0.0) {
        x = -x;
        negate = true;
    }
    double result;
    if (x > 20.0) {
        result = 0.5 * FastMath.exp(x);
    } else {
        double[] hiPrec = new double[2];
        FastMath.exp(x, 0.0, hiPrec);
        double ya = hiPrec[0] + hiPrec[1];
        double yb = -(ya - hiPrec[0] - hiPrec[1]);
        if (x < 1.0) {
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
            ya = ra + rb;
            yb = -(ya - ra - rb);
        } else {
            double e = 1.0 + ya;
            double[] hiPrec2 = new double[2];
            double[] loPrec = new double[2];
            double fact = 1.0;
            double xsq = x * x;
            for (int i = 2; i < 42; i += 2) {
                fact *= i - 1;
                fact *= i;
                double cur = xsq / fact;
                loPrec[0] = cur - (cur - xsq / ((i - 1) * i));
                loPrec[1] = xsq * ((loPrec[0] - cur) / cur);
                FastMath.multiply(ya, yb, hiPrec2);
                ya = hiPrec2[0] + hiPrec2[1];
                yb = -(ya - hiPrec2[0] - hiPrec2[1]);
                FastMath.multiply(ya, e, hiPrec2);
                ya = hiPrec2[0] + hiPrec2[1];
                yb += hiPrec2[0] - ya;
                yb += hiPrec2[1];
                FastMath.multiply(ya, loPrec[0], hiPrec2);
                double temp = ya + hiPrec2[0];
                yb += -(temp - ya - hiPrec2[0]);
                ya = temp;
                FastMath.multiply(ya, loPrec[1], hiPrec2);
                temp = ya + hiPrec2[0];
                yb += -(temp - ya - hiPrec2[0]);
                ya = temp;
            }
        }
        result = ya + yb;
        result *= 0.5;
    }
    return negate ? -result : result;
}