## Fixed Function 1
public static int gcd(final int p, final int q) {
    int u = Math.abs(p);
    int v = Math.abs(q);
    if ((u == 0) || (v == 0)) {
        return (Math.abs(u) + Math.abs(v));
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
    while (u != 0 && v != 0) {
        while ((v & 1) == 0) {
            v /= 2;
        }
        while ((u & 1) == 0) {
            u /= 2;
        }
        if (u >= v) {
            u = u - v;
        } else {
            v = v - u;
        }
    }
    int gcd = Math.abs((u + v)); // only one of them will be != 0
    return gcd;
}

## Fixed Function 2
public static int lcm(int a, int b) {
    if (a == 0 || b == 0) {
        return 0;
    }
    int gcd = gcd(a, b);
    int lcm = Math.abs(Math.multiplyExact(a / gcd, b));
    return lcm;
}