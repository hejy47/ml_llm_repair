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
    while ((u & 1) == 0 && (v & 1) == 0 && k < 31) {
        // while u and v are both even...
        // cast out twos.
        u /= 2;
        // cast out twos.
        v /= 2;
        // cast out twos.
        k++;
    }
    if (k == 31) {
        throw new ArithmeticException("overflow: gcd is 2^31");
    }
    // B2. Initialize: u and v have been divided by 2^k and at least
    //     one is odd.
    int t = ((u & 1) == 1) ? u : v; // fixed swapping issue 
    // t negative: u was odd, v may be even (t replaces v)
    // t positive: u was even, v is odd (t replaces u)
    do {
                // B4/B3: cast out twos from t.
        while ((t & 1) == 0) {
            // while t is even..
            // cast out twos
            t /= 2;
        }
        // B5 [reset max(u,v)]
        if (t > 0) {
            u = -t;
        } else {
            v = t;
        }
        // B6/B3. at this point both u and v should be odd.
        t = (v - u) / 2;
        // |u| larger: t positive (replace u)
        // |v| larger: t negative (replace v)
    } while (t != 0);
    // gcd is u*2^k
    return -u * (1 << k);
}