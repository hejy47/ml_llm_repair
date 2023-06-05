## Fixed Function 1
public static double distance(int[] p1, int[] p2) {
    int sum = 0;
    for (int i = 0; i < p1.length; i++) {
        final int dp = p1[i] - p2[i];
        sum += dp * dp;
    }
    return Math.sqrt((double)sum);
} 

# Explanation
The issue with the original implementation is that the `Math.sqrt` function accepts only double argument, and hence it was necessary to cast the sum of squares to a double before calling `Math.sqrt()`. Without the cast, an error would be thrown.