## Fixed Function 1
public double getSumSquaredErrors() {
    if (sumXX == 0) {
        return 0;
    }
    return sumYY - sumXY * sumXY / sumXX;
}

The fix for this function is to check if the value of `sumXX` is zero before returning the calculation. If `sumXX` is zero, then the function should return 0 to avoid division by zero errors.