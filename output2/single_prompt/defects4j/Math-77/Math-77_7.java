## Fixed Function 1
@Override
public double getLInfNorm() {
    double max = 0;
    for (double a : data) {
        max = Math.max(max, Math.abs(a)); // change += to Math.max function
    }
    return max;
}

## Fixed Function 2
public double getLInfNorm() {
    double max = 0;
    DoubleIterator iter = entries.iterator(); // use DoubleIterator instead of Iterator
    while (iter.hasNext()) {
        max = Math.max(max, Math.abs(iter.nextDouble())); // use nextDouble() instead of value() and update max
    }
    return max;
}