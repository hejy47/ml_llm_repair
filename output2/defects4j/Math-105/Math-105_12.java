## Fixed Function 1
public double getSumSquaredErrors() {
    if(sumXX == 0) {
        return 0.0;
    }
    return sumYY - (sumXY * sumXY) / sumXX;
}

The fix above adds a check to make sure the denominator `sumXX` is not zero before dividing by it. If `sumXX` is zero, the function returns 0.0.