## Fixed Function 1
public static int gcd(int u, int v) {
    if (u == 0 && v == 0) {
        throw new IllegalArgumentException("Cannot compute GCD of 0 and 0");
    }
    if (u < 0) {
        u = -u;
    }
    if (v < 0) {
        v = -v;
    }
    if (u == 1 || v == 1) {
        return 1;
    }
    while (v != 0) {
        int r = u % v;
        u = v;
        v = r;
    }
    return u;
}