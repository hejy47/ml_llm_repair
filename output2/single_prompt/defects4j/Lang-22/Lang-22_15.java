## Fixed Function 1
private static int greatestCommonDivisor(int u, int v) {
    // From Commons Math:
    //if either operand is abs 1, return 1:
    if (Math.abs(u) <= 1 || Math.abs(v) <= 1) {
        return 1;
    }
    // keep u and v negative, as negative integers range down to
    // -2^31, while positive numbers can only be as large as 2^31-1
    // (i.e. we can't necessarily negate a negative number without
    // overflow)
    // make u negative
    if (u > 0) {
        u = -u;
    }
    // make v negative
    if (v > 0) {
        v = -v;
    }
    // B1. [Find power of 2]
    int k = 0;
    while ((u != 0) && (v != 0) && (k < 31)) {
        // while u and v are both nonzero and unshifted...
        // cast out twos
        u /= 2;
        // cast out twos
        v /= 2;
        // add to 2^k
        k++;
    }
    if (k == 31) {
        throw new ArithmeticException("overflow: gcd is 2^31");
    }
    // B2. Initialize: u and v have been divided by 2^k and at least
    //     one is odd.
    int t = ((u & 1) == 1) ? u : v;
    // t negative: u was odd, v may be even (t replaces u)
    // t positive: u was even, v is odd (t replaces v)
    // apply the sign
    t = -t;
    // B3 / B4 scaling
    while (k > 0) {
        // shift up to 2^k
        t *= 2;
        k--;
        // cast out twos
        while ((t > 0) && ((t & (1 << 30)) != 0)) {
            // cast out twos
            t /= 2;
        }
    }
    // return abs value of t, which is the gcd
    return Math.abs(t);
}