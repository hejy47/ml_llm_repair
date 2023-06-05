## Fixed Function 1
public double getSumSquaredErrors() {
    if(sumXX == 0) {
        return 0.0;
    }
    return sumYY - sumXY * sumXY / sumXX;
}

# Explanation
The fixed function checks if `sumXX` is `0` before performing the calculation. If it is `0`, it returns `0.0` to avoid dividing by zero. This fixes the potential division by zero error.