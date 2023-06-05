## Fixed Function 1
@Override
public double getLInfNorm() {
    double max = 0;
    for (double a : data) {
        max = Math.max(max, Math.abs(a)); // fixed code
    }
    return max;
}

## Fixed Function 2
public double getLInfNorm() {
    double max = 0;
    DoubleCursor iter = entries.cursor(); // fixed code
    while (iter.moveNext()) { // fixed code
        max = Math.max(max, Math.abs(iter.value())); // fixed code
    }
    return max;
}