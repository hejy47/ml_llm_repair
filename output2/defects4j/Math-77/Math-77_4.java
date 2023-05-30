## Fixed Function 1
@Override
public double getLInfNorm() {
    double max = 0;
    for (double a : data) {
        max = Math.max(max, Math.abs(a)); // fixed comparison
    }
    return max;
}

## Fixed Function 2
public double getLInfNorm() {
    double max = 0;
    TObjectDoubleIterator iter = entries.iterator();
    while (iter.hasNext()) {
        iter.advance();
        max = Math.max(max, Math.abs(iter.value())); // fixed value comparison
    }
    return max;
}