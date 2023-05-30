## Fixed Function 1
public static int gcd(final int p, final int q) {
    int u = Math.abs(p);
    int v = Math.abs(q);
    if ((u == 0) || (v == 0)) {
        return Math.abs(u) + Math.abs(v);
    }
    int shift;
    for (shift = 0; ((u | v) & 1) == 0; ++shift) {
        u >>= 1;
        v >>= 1;
    }
    while ((u & 1) == 0) {
        u >>= 1;
    }
    do {
        while ((v & 1) == 0) {
            v >>= 1;
        }
        if (u > v) {
            int t = v; v = u; u = t;
        }
        v = v - u;
    } while (v != 0);
    return u << shift;
}

## Fixed Function 2
public static int lcm(int a, int b) {
    if (a == 0 || b == 0) {
        return 0;
    }
    int lcm = Math.abs(a / gcd(a, b) * b);
    return lcm;
}