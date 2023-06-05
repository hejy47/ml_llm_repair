public static int gcd(int u, int v) {
    if (u * v == 0) {
        return (Math.abs(u) + Math.abs(v));
    }
    // keep u and v negative, as negative integers range down to
    // -2^31, while positive numbers can only be as large as 2^31-1
    // (i.e. we can't necessarily negate a negative number without
    // overflow)
    if (u > 0) {
        u = -u;
    }
    // make u negative
    if (v > 0) {
        v = -v;
    }
    // make v negative
    // B1. [Find power of 2]
    int k = 0;
    while ((u & 1) == 0 && (v & 1) == 0 && k < 31) {
        // while u and v are
        // both even...
        u /= 2;
        v /= 2;
        // cast out twos.
        k++;
    }
    if (k == 31) {
        throw new ArithmeticException("overflow: gcd is 2^31");
    }
    // B2. Initialize: u and v have been divided by 2^k and at least
    // one is odd.
    int t;
    while (v != 0) {
        if (-u < -v) {
            t = u;
            u = -v;
            v = t;
        }
        u -= v;
    }
    // gcd is u*2^k
    return -(u << k);
}