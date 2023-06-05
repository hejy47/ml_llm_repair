public static double pow(double x, double y) {
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
        if (y != (long) y || y % 2 == 0) {
            // x is negative, y is not integer, or y is even
            return Double.NaN;
        }
        // x is negative, y is an odd integer, so flip the sign of the result
        return -pow(-x, y);
    }
    double result = 1.0;
    while (y != 0) {
        if ((y & 1) == 1) {
            result *= x;
        }
        x *= x;
        y >>= 1;
    }
    return result;
}