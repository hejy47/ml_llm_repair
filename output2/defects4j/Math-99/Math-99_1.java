## Fixed Function 1
public static int gcd(final int p, final int q) {
    int u = p;
    int v = q;
    if ((u == 0) || (v == 0)) {
        return (Math.abs(u) + Math.abs(v));
    }
    // make u and v positive
    u = Math.abs(u);
    v = Math.abs(v);
    // B1. [Find power of 2]
    int k = 0;
    while ((u % 2 == 0) && (v % 2 == 0) && k < 31) {
        // while u and v are
        // both even...
        u /= 2;
        v /= 2;
        // cast out twos.
        k++;
    }
    if (k == 31) {
        throw MathRuntimeException.createArithmeticException("overflow: gcd({0}, {1}) is 2^31", new Object[] { p, q });
    }
    // B2. Initialize: u and v have been divided by 2^k and at least
    // one is odd.
    int t = ((u % 2 == 1) ? v : -(u / 2));
    // t negative: u was odd, v may be even (t replaces v)
    // t positive: u was even, v is odd (t replaces u)
    do {
        // B3 and B4: divide t by 2 until odd
        while (t % 2 == 0) {
            t /= 2;
        }
        // B5 [reset u or v]
        if (t > 0) {
            u = t;
        } else {
            v = -t;
        }
        // B6. Subtract smaller from larger until they are equal
        t = v - u;
    } while (t != 0);
    // gcd is u*2^k
    return u << k;
}

## Fixed Function 2
public static int lcm(int a, int b) {
    if (a == 0 || b == 0) {
        return 0;
    }
    int lcm = Math.abs(a / gcd(a, b)) * Math.abs(b);
    return lcm;
}