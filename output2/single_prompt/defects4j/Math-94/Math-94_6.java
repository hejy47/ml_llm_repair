## Fixed Function 1
public static int gcd(int u, int v) {
    if (u == 0)
        return Math.abs(v);
    if (v == 0)
        return Math.abs(u);
    int a = Math.abs(u), b = Math.abs(v);
    while (b != 0) {
        int temp = b;
        b = a % b;
        a = temp;
    }
    return a;
}