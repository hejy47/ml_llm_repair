## Fixed Function 1
public double getSumSquaredErrors() {
    if(sumXX == 0) {
        return 0;
    } else {
        return sumYY - sumXY * sumXY / sumXX;
    }
}