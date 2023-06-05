## Fixed Function 1
public double getSumSquaredErrors() {
    if(sumXX == 0){
        return 0.0;
    }
    return sumYY - (sumXY * sumXY) / sumXX;
}

The fixed function includes a check to ensure that if `sumXX` is zero, the function will return zero instead of throwing an error or returning an incorrect value. It also uses parentheses to ensure that the division is performed before the multiplication, ensuring that the order of operations is correct.