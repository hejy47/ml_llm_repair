## Fixed Function 1
public double getSumSquaredErrors() {
    if (sumXX == 0) {
        return 0.0;
    }
    return sumYY - (sumXY * sumXY) / sumXX;
} 

Explanation: The original buggy function did not check if the denominator (sumXX) was 0 before dividing, which could result in a division by zero error. The fixed function checks for this case and returns 0.0 to avoid the error.