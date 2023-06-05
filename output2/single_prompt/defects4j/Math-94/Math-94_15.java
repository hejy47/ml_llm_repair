## Fixed Function 1
public static int gcd(int u, int v) {
    if (u == 0) return v;
    if (v == 0) return u;
    u = Math.abs(u);
    v = Math.abs(v);
    int k = 0;
    while (((u | v) & 1) == 0) {
        u >>= 1;
        v >>= 1;
        k++;
    }
    while ((u & 1) == 0) {
        u >>= 1;
    }
    do {
        while ((v & 1) == 0) {
            v >>= 1;
        }
        if (u > v) {
            int temp = u;
            u = v;
            v = temp;
        }
        v = v - u;
    } while (v != 0);

    return u << k;
}