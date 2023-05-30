## Fixed Function 1
public int compareTo(Fraction object) {
    double nOd = this.doubleValue();
    double dOn = object.doubleValue();
    if (nOd < dOn) {
        return -1;
    } else if (nOd > dOn) {
        return 1;
    }
    return 0;
} 

Explanation: 
- We need to use "this" keyword to refer to the calling object instead of calling the method directly on the object.
- We added curly braces to make the code more readable and avoid errors with the ternary operator.