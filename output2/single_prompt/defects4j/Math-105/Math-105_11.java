## Fixed Function 1
public double getSumSquaredErrors() {
    if(sumXX == 0) {
        return 0;
    }
    return sumYY - (sumXY * sumXY / sumXX);
}