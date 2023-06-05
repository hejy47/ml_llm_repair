## Fixed Function 1
public int compareTo(Fraction object) {
    double nOd = this.doubleValue();
    double dOn = object.doubleValue();
    if (nOd < dOn) {
        return -1;
    } else if (nOd > dOn) {
        return 1;
    } else {
        return 0;
    }
} 

## Fixed Function 2
public int compareTo(Fraction object) {
    double nOd = this.numerator / this.denominator;
    double dOn = object.numerator / object.denominator;
    if (nOd < dOn) {
        return -1;
    } else if (nOd > dOn) {
        return 1;
    } else {
        return 0;
    }
} 

Note: There are two possible fixes for the compareTo() function in the Math project. The first fix uses the doubleValue() method to ensure that the fractions are compared as doubles. The second fix directly divides the numerator by the denominator to get the decimal representation of the fraction.