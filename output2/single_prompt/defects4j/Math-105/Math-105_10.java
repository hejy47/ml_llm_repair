## Fixed Function 1
public double getSumSquaredErrors() {
    if(sumXX == 0){
        return Double.NaN;
    }
    return sumYY - sumXY * sumXY / sumXX;
}

## Explanation
The problem with the original function is that it could result in division by zero if `sumXX` is equal to zero, which would cause the program to crash. To fix the function, we need to add a check for whether `sumXX` is zero before attempting to divide by it. In this case, we can return `Double.NaN` to indicate that the result is undefined.