## Fixed Function 1
public double getSumSquaredErrors() {
    if (sumXX != 0) {
        return sumYY - sumXY * sumXY / sumXX;
    } else {
        return 0.0;
    }
}
## Explanation
The original code does not check if the denominator `sumXX` is zero, which would result in a divide by zero error. The fix adds a check to return 0.0 if `sumXX` is zero, avoiding the error.