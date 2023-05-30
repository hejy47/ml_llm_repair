## Fixed Function 1
@Override
public double getLInfNorm() {
    double max = 0;
    for (double a : data) {
        max = Math.max(max, Math.abs(a)); // fixing the calculation of max value
    }
    return max;
}

## Fixed Function 2
public double getLInfNorm() {
    double max = 0;
    DoubleIterator iter = entries.values().iterator(); // using the values iterator to iterate through the Map values
    while (iter.hasNext()) {
        max = Math.max(max, Math.abs(iter.nextDouble())); // fixing the calculation of max value
    }
    return max;
}