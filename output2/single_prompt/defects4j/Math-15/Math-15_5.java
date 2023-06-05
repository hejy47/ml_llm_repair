public static double pow(double x, double y) {
    final double[] lns = new double[2];
    if (y == 0.0) {
        return 1.0;
    }
    if (x != x) {
        // X is NaN
        return x;
    }
    if (x == 0) {
        long bits = Double.doubleToLongBits(x);
        if ((bits & 0x8000000000000000L) != 0) {
            // -zero
            long yi = (long) y;
            if (y < 0 && y == yi && (yi & 1) == 1) {
                return Double.NEGATIVE_INFINITY;
            }
            if (y > 0 && y == yi && (yi & 1) == 1) {
                return -0.0;
            }
        }
        if (y < 0) {
            return Double.POSITIVE_INFINITY;
        }
        if (y > 0) {
            return 0.0;
        }
        return Double.NaN;
    }
    if (x == Double.POSITIVE_INFINITY) {
        if (y != y) {
            // y is NaN
            return y;
        }
        if (y < 0.0) {
            return 0.0;
        } else {
            return Double.POSITIVE_INFINITY;
        }
    }
    if (y == Double.POSITIVE_INFINITY) {
        if (x * x == 1.0) {
            return Double.NaN;
        }
        if (x * x > 1.0) {
            return Double.POSITIVE_INFINITY;
        } else {
            return 0.0;
        }
    }
    if (x == Double.NEGATIVE_INFINITY) {
        if (y != y) {
            // y is NaN
            return y;
        }
        if (y < 0) {
            long yi = (long) y;
            if (y == yi && (yi & 1) == 1) {
                return -0.0;
            }
            return 0.0;
        }
        if (y > 0) {
            long yi = (long) y;
            if (y == yi && (yi & 1) == 1) {
                return Double.NEGATIVE_INFINITY;
            }
            return Double.POSITIVE_INFINITY;
        }
    }
    if (y == Double.NEGATIVE_INFINITY) {
        if (x * x == 1.0) {
            return Double.NaN;
        }
        if (x * x < 1.0) {
            return Double.POSITIVE_INFINITY;
        } else {
            return 0.0;
        }
    }
    if (x < 0) {
        // y is an integer in this case
        if (y >= TWO_POWER_52 || y <= -TWO_POWER_52) {
            return pow(-x, y);
        }
        if (y == (long) y) {
            // If y is an integer
            return ((long) y & 1) == 0 ? pow(-x, y) : -pow(-x, y);
        } else {
            return Double.NaN;
        }
    }
    double ya;
    double yb;
    if (y < 8e298 && y > -8e298) {
        double tmp1 = y * HEX_40000000;
        ya = y + tmp1 - tmp1;
        yb = y - ya;
    } else {
        double tmp1 = y * 9.31322574615478515625E-10;
        double tmp2 = tmp1 * 9.31322574615478515625E-10;
        ya = (tmp1 + tmp2 - tmp1) * HEX_40000000 * HEX_40000000;
        yb = y - ya;
    }
    final double lores = log(x, lns);
    if (Double.isInfinite(lores)) {
        // don't allow this to be converted to NaN
        return lores;
    }
    double lna = lns[0];
    double lnb = lns[1];
    double aa, ab, ac, ad, ae, af;

    // split y into 2 parts
    double tmp1 = HEX_40000000 * y;
    double tmp2 = y + tmp1 - tmp1;
    double y1 = tmp2;
    double y2 = y - y1;

    // Compute ln(x) * y1 using Accurate High Order Product
    aa = lna * y1;
    ab = lna * y2 + lnb * y1;
    ac = lnb * y2;
    ad = aa + ab;
    ae = aa - ad + ab + ac;
    af = ad - aa;
    aa = ad;
    ab = ae;
    ac = af;
    ad = aa + ab;
    ae = aa - ad + ab + ac;
    af = ad - aa;
    aa = ad;
    ab = ae;
    ac = af;
    ad = aa + ab;
    ae = aa - ad + ab + ac;
    double lnproduct = ad + ae;

    // If ln(x) * y1 is infinite, the result is infinite.
    if (Double.isInfinite(lnproduct)) {
        return processAsSpecial(x, lnproduct);
    }

    // Compute exp(ln(x) * y1) using Accurate High-Order Product
    double z = lnproduct * 1.4426950408889634;
    double zf = z - 512 * HEX_40000000 * 0.6931471805599453;
    double z2 = zf - z;
    final double result = exp(lnproduct, z2 + zf, null);
    return processResult(x, lnproduct, result);
}

private static double processAsSpecial(double x, double lnproduct) {
    // If ln(x) * y1 is infinite, the result is infinite.
    if (Double.isNaN(lnproduct)) {
        return Double.NaN;
    }
    if (x == 1.0 || x == -1.0) {
        return Double.NaN;
    }
    if (lnproduct < 0) {
        return 0.0;
    }
    return Double.POSITIVE_INFINITY;
}

private static double processResult(double x, double lnproduct, double result) {
    if (result == 0.0) {
        return result;
    }
    if (Double.isNaN(result)) {
        if (Double.isNaN(lnproduct)) {
            return Double.NaN;
        }
        return processAsSpecial(x, lnproduct);
    }

    // Compute exp(ln(x) * y2) using normal means
    if (lnproduct == 0.0 || result == 1.0) {
        return result;
    }
    double xp = StrictMath.exp(lnproduct);
    double yp = result / xp;
    double tmp1 = yp * HEX_40000000;
    double ya = yp + tmp1 - tmp1;
    double yb = yp - ya;
    tmp1 = xp * HEX_40000000;
    double za = xp + tmp1 - tmp1;
    double zb = xp - za;

    // Compute (xp + yp)(xp - yp)
    double e = xp * xa - ya * za;
    double f = xa * zb + xb * za;
    double tmp2 = ya * yb;
    e += tmp2;
    f += xb * yb;

    // Compute exp(ln(x) * y) = exp(ln(x) * y1) * exp(ln(x) * y2)
    return exp(lnproduct, e + f, null);
}