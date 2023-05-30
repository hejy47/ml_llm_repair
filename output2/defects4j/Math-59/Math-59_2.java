## Fixed Function 1
public static float max(final float a, final float b) {
    return (a >= b) ? a : (Float.isNaN(a + b) ? Float.NaN : b);
}