## Fixed Function 1
public double getSumSquaredErrors() {
    if(sumXX == 0){
        return -1; // or throw an exception
    }
    return sumYY - (sumXY * sumXY) / sumXX;
} 
# This function now checks if sumXX is zero. Dividing by zero produces undefined behavior, so it is recommended to provide a fallback action such as returning -1 or throwing an exception.