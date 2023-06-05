private static int greatestCommonDivisor(int u, int v) {
    if (Math.abs(u) <= 1 || Math.abs(v) <= 1) {
        return 1;
    }
    if (u == 0) {
        return v;
    }
    if (v == 0) {
        return u;
    }
    if (u < 0) {
        u = -u;
    }
    if (v < 0) {
        v = -v;
    }
    int k = 0;
    while ((u & 1) == 0 && (v & 1) == 0 && k < 31) {
        u /= 2;
        v /= 2;
        k++;
    }
    if (k == 31) {
        throw new ArithmeticException("overflow: gcd is 2^31");
    }
    while (u != v) {
        if (u > v) {
            u -= v;
        } else {
            v -= u;
        }
    }
    return u * (1 << k);
}