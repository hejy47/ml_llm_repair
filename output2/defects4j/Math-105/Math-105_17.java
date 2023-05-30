## Fixed Function 1
public double getSumSquaredErrors() {
    if(sumXX == 0) {
        return 0; //to avoid divide by zero error
    }
    return sumYY - (sumXY * sumXY) / sumXX;
}