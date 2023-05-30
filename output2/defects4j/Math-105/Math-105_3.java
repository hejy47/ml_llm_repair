## Fixed Function 1
public double getSumSquaredErrors() {
    if(sumXX == 0){
        return 0;
    }
    else{
        return sumYY - sumXY * sumXY / sumXX;
    }
} 

Note: The fix checks for division by zero by verifying if sumXX equals 0. If sumXX equals 0, the function returns 0 instead of evaluating the division.