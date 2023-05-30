## Fixed Function 1
@Override
public double getLInfNorm() {
    double max = 0;
    for (double a : data) {
        max = Math.max(max, Math.abs(a)); //fix: use Math.max to update max value
    }
    return max;
}

## Fixed Function 2
public double getLInfNorm() {
    double max = 0;
    Iterator iter = entries.iterator();
    while (iter.hasNext()) {
        double val = iter.value(); //fix: get the value and assign it to a variable
        max = Math.max(max, Math.abs(val)); //fix: use Math.max to update max value with absolute value of the variable
        iter.advance();
    }
    return max;
}