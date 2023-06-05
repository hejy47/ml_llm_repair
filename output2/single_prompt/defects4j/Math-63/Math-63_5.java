## Fixed Function 1
public static boolean equals(double x, double y) {
    return (Double.isNaN(x) && Double.isNaN(y)) || Math.abs(x - y) < 1E-8;
} 

## Explanation
The original function does not correctly compare two double values in cases where they are not exactly equal due to floating point imprecision. The fixed function uses the absolute difference between the two values and checks if it is within a small tolerance of 1E-8 to determine if they are equal.