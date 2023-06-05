## Fixed Function 1
public static boolean equals(double x, double y) {
    return (Double.isNaN(x) && Double.isNaN(y)) || (Math.abs(x - y) < 0.0001);
} 

#Explanation
The problem with the original implementation is that it is using exact equality to compare two double values, which can be problematic due to rounding errors. The fixed implementation instead checks whether the two values are within a small tolerance epsilon.