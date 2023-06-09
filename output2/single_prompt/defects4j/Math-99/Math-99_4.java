## Fixed Function 1
public static int gcd(final int p, final int q) {
    int u = p;
    int v = q;
    if ((u == 0) || (v == 0)) {
        return (Math.abs(u) + Math.abs(v));
    }
    if (u > 0) {
        u = -u;
    }
    if (v > 0) {
        v = -v;
    }
    int k = 0;
    while ((u & 1) == 0 && (v & 1) == 0 && k < 31) {
        u /= 2;
        v /= 2;
        k++;
    }
    if (k == 31) {
        throw MathRuntimeException.createArithmeticException("overflow: gcd({0}, {1}) is 2^31", new Object[] { p, q });
    }
    int t = ((u & 1) == 1) ? v : -(u / 2);
    do {
        while ((t & 1) == 0) {
            t /= 2;
        }
        if (t > 0) {
            u = -t;
        } else {
            v = t;
        }
        t = (v - u) / 2;
    } while (t != 0);
    return -u * (1 << k);
}

## Fixed Function 2
public static int lcm(int a, int b) {
    if (a == 0 || b == 0) {
        return 0;
    }
    int lcm = Math.abs(a / gcd(a, b)) * Math.abs(b);
    return lcm;
}