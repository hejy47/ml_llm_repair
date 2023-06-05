## Fixed Function 1
public static boolean equals(double x, double y) {
    return (Double.isNaN(x) && Double.isNaN(y)) || (Math.abs(x-y) < 1e-10);
}

# The fix for this function changes the comparison from an exact equality (using `==`) to a comparison that checks for a small difference between the two values. This avoids potential problems that can arise due to floating point precision errors when comparing two floating point values for exact equality.