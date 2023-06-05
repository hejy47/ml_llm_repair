## Fixed Function 1
public static double distance(int[] p1, int[] p2) {
    double sum = 0; // Changed to double to avoid rounding errors
    for (int i = 0; i < p1.length; i++) {
        final double dp = p1[i] - p2[i]; // Changed to double to avoid rounding errors
        sum += dp * dp;
    }
    return Math.sqrt(sum);
}