## Fixed Function 1
public double getSumSquaredErrors() {
    if (sumXX == 0) { //check for divide by 0 error
        return 0.0;
    }
    return sumYY - sumXY * sumXY / sumXX;
}