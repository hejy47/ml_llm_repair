## Fixed Function 1
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
        // if v is odd, swap v and u
        if (u % 2 == 0) {
            if (v % 2 == 0) {
                // both u and v are even, divide by 2
                u /= 2;
                v /= 2;
                k++;
            } else {
                // u is even, v is odd, divide u by 2
                u /= 2;
            }
        } else if (v % 2 == 0) {
            // u is odd, v is even, divide v by 2
            v /= 2;
        } else {
            // both u and v are odd, subtract u from v
            // (if u > v) and divide result by 2
            t = Math.abs(u - v) / 2;
            if (u > v) {
                u = -t;
                v = -v;
            } else {
                u = -v;
                v = -t;
            }
        }
    }
    // gcd is u*2^k
    return -u * (1 << k);
}