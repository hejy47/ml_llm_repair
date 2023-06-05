## Fixed Function 1
public static double distance(int[] p1, int[] p2) {
    double sum = 0; //change variable type to double
    for (int i = 0; i < p1.length; i++) {
        final int dp = p1[i] - p2[i];
        sum += dp * dp;
    }
    return Math.sqrt(sum);
}