## Fixed Function 1
public double getSumSquaredErrors() {
    if (sumXX == 0) {
        return 0.0;
    } else {
        return sumYY - sumXY * sumXY / sumXX;
    }
} 

Note: The fix includes a check for division by zero, where if sumXX is zero, the function returns 0.0 instead of trying to calculate the expression.