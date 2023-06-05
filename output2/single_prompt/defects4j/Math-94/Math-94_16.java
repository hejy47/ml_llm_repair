## Fixed Function 1
public static int gcd(int u, int v) {
    if (u * v == 0) {
        return (Math.abs(u) + Math.abs(v));
    }
    int signU = u > 0 ? 1 : -1; // save the sign of u
    int signV = v > 0 ? 1 : -1; // save the sign of v
    u = Math.abs(u);
    v = Math.abs(v);

    if (u < v) {
        int temp = u; // swap u and v
        u = v;
        v = temp;
    }
    if (v == 0) {
        return u * signU; // gcd(u, 0) = u
    }

    // B1. [Find power of 2]
    int k = 0;
    while ((u & 1) == 0 && k < 31) {
        // while u is even...
        u /= 2;
        k++;
    }

    // B2. Initialize: v is odd and u is now odd too (gcd(u, v) = gcd(u', v), u' = u/2^k)
    while (v != 0) {
        while ((v & 1) == 0) {
            // while v is even...
            v /= 2;
        }
        int difference = Math.abs(u - v) / 2;
        if (u >= v) {
            u = v + difference; // u is even
        } else {
            v = u + difference; // v is even
        }
    }

    return u * (1 << k) * signU;
}