## Fixed Function 1
public double getSumSquaredErrors() {
    if(sumXX == 0){
        return 0.0;
    }
    return sumYY - sumXY * sumXY / sumXX;
}

Explanation: 
The buggy function does not handle the case when `sumXX` is equal to 0 resulting in a division by zero error. The fixed function checks if `sumXX` is equal to 0 and returns 0.0 to avoid the error.