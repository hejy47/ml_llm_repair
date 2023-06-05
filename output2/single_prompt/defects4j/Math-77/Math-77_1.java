## Fixed Function 1
@Override
public double getLInfNorm() {
    double max = 0;
    for (double a : data) {
        max = Math.max(max, Math.abs(a));
    }
    return max;
}

## Fixed Function 2
public double getLInfNorm() {
    double max = 0;
    DoubleIterator iter = entries.iterator();
    while (iter.hasNext()) {
        max = Math.max(max, Math.abs(iter.nextDouble()));
    }
    return max;
}