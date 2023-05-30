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
    while ((u & 1) == 0 && (v & 1) == 0 && k < 31) {
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
    int t = ((u & 1) == 1) ? v : -(u / 2);
    // t negative: u was odd, v may be even (t replaces v)
    // t positive: u was even, v is odd (t replaces u)
    do {
        // B3: cast out twos
        while ((t & 1) == 0) {
            t /= 2;
        }
        // B4: simplify t
        if (t > 0) {
            u = -t;
        } else {
            v = t;
        }
        // B5. Both u and v are odd. If v > u then we need to swap
        // u and v.
        if (v > u) {
            int swap = u;
            u = v;
            v = swap;
        }
        // B6. set v = v - u (which is even). Then cast out twos.
        t = (v - u) / 2;
    } while (t != 0);
    // undo the previous negations
    return -u * (1 << k);
}

## Fixed Function 2
public static int lcm(int a, int b) {
    if (a == 0 || b == 0) {
        return 0;
    }
    int lcm = Math.abs(mulAndCheck(a / gcd(a, b), b));
    // fix for potential integer overflow
    if (lcm == Integer.MIN_VALUE) {
        throw MathRuntimeException.createArithmeticException("overflow: lcm({0}, {1}) is 2^31", new Object[]{a, b});
    }
    return lcm;
}