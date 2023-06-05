## Fixed Function 1
public static int gcd(final int p, final int q) {
    int u = Math.abs(p);
    int v = Math.abs(q);
    if ((u == 0) || (v == 0)) {
        return u + v;
    }
    int k = 0;
    while (u > 1 && v > 1 && k < 31) {
        if (u % 2 == 0 && v % 2 == 0) {
            u /= 2;
            v /= 2;
            k++;
        } else if (u % 2 != 0 && v % 2 != 0) {
            if (u >= v) {
                u = (u - v) / 2;
            } else {
                int tmp = u;
                u = (v - u) / 2;
                v = tmp;
            }
        } else if (u % 2 == 0) {
            u /= 2;
        } else {
            v /= 2;
        }
    }
    return (int) Math.pow(-1, k) * u * v;
}

## Fixed Function 2
public static int lcm(int a, int b) {
    if (a == 0 || b == 0) {
        return 0;
    }
    int gcd = gcd(a, b);
    if (gcd == 0) {
        return 0;
    }
    return Math.abs(a / gcd * b);
}