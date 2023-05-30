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
        // y is an even integer in this case
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
    double aa = lna * ya, ab = 0, ac = 0, ad = 0, ae = 0, af = 0, ag = 0, ah = 0, ak = 0;
    if (x == 1) {
        final double t1 = y * HEX_40000000;
        double t2 = y + t1 - t1;
        ab = t2 * lna;
        ac = t2 * t2 * (0.5 * lna);
        ad = (y - t2) * lna;
    } else {
        double z = lna * y;
        double tmp1 = lna * HEX_40000000;
        double tmp2 = lna + tmp1 - tmp1;
        lnb += lna - tmp2;
        lna = tmp2;
        aa = lna * ya;
        ab = lna * yb + lnb * ya;
        double _aa = aa, _ab = ab;
        ac = _aa * INV_LN_2_SQUARED + _ab * 4.969524976307135E-10;
        ad = _aa * INV_LN_2 + _ab * 1.539172581798108E-12;
        aa = aa * y + lna * ya * ya;
        ab = ab * y + lnb * ya * ya;
        _aa = aa, _ab = ab;
        ac += _aa * 2.967721961301243E-10 + _ab * 1.332867188776738E-10;
        ad += _aa * 0.6931471461993334 + _ab * 1.908214929270587E-10;
        aa = aa * y + lna * ya * yb * 2.0;
        ab = ab * y + lnb * ya * yb * 2.0;
        _aa = aa, _ab = ab;
        ac += _aa * 2.359208954715764E-10 + _ab * 2.222219860718627E-12;
        ad += _aa * 5.00825446469529E-9 + _ab * 2.547046017719519E-10;
        aa = aa * y + lna * yb * yb;
        ab = ab * y + lnb * yb * yb;
        _aa = aa, _ab = ab;
        ac += _aa * 1.177830356722936E-9 + _ab * 2.111195911656856E-11;
        ad += _aa * 0.08333333333343307 + ad * 0.6931471805599385;
    }
    ae = ya + yb;
    af = 0.5 * ya + ae * 0.5;
    double agI = af + ya * 0.5;
    double agII = ae * INV_LN_2;
    ah = agI + agII;
    double akI = agI - ah;
    double akII = agII - akI;
    ak = ((ya - akI) + (yb - akII)) * INV_LN_2_SQUARED;
    final double result = exp(lna, af, null)
            * ((((ad + ac) + (ae + af) * ak) + (ah * y)) + exp(lna, ak, null) * exp(lna, ak, null) * 0.5);
    return result;
}