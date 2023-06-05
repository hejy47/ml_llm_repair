## Fixed Function 1
public static boolean equals(double x, double y) {
    if(Double.isNaN(x) && Double.isNaN(y)) {
        return true;
    } else if(Math.abs(x - y) < 0.0001) {
        return true;
    }
    return false;
}

Explanation: We first check if both inputs are NaN and return true if they are. If not, we check if the absolute difference between the two inputs is less than 0.0001 and return true if it is. This is because direct comparison of doubles can lead to inaccuracies due to rounding errors.