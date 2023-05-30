## Fixed Function 1
public static int gcd(final int p, final int q) {
    int u = p;
    int v = q;
    if ((u == 0) || (v == 0)) {
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
    while ((u & 1) == 0 && (v & 1) == 0 && k < 30) {
        // while u and v are
        // both even...
        u /= 2;
        v /= 2;
        // cast out twos.
        k++;
    }

    if (k == 30) {
        throw MathRuntimeException.createArithmeticException(
            "overflow: gcd({0}, {1}) is 2^31", new Object[] {p, q});
    }
    // B2. Initialize: u and v have been divided by 2^k and at least
    // one is odd.
    int t;
    while (u != 0) {
        // u is odd
        if ((u & 1) == 1) {

            // so we can assume v is odd.
            // If not, we could change roles of u and v.
            if (v > 0) {
                u = -u;
            }
            // We know that u and v are both odd.
            // Use u to store the difference of the two, which must be
            // even. Half this difference (arithmetically a right shift)
            // to get v/2. Note that u is negative so we need to be careful
            // when dividing.
            t = (v - u) / 2;

            // t will be negative when u is greater than v;
            // in this case, restore u to its original value.
            u = (u + v) / 2;

            v = t;
        } else {
            // u is even
            if (v > 0) {
                u /= 2;
            }

            // either u or v is even, or both
            if (v < 0) {
                v += -u;
            }

            if (u > 0) {
                u /= 2;
            }
            k++;
        }
    }
    return -v * (1 << k);
}

## Fixed Function 2
public static int lcm(int a, int b) {
    if (a == 0 || b == 0) {
        return 0;
    }
    int lcm = Math.abs(Math.multiplyExact(a / gcd(a, b), b));
    return lcm;
}