## Fixed Function 1
@Override
public double getLInfNorm() {
    double max = 0;
    for (double a : data) {
        max = Math.max(max, Math.abs(a)); // changed from += to Math.max()
    }
    return max;
}

## Fixed Function 2
public double getLInfNorm() {
    double max = 0;
    DoubleObjectCursor<Map.Entry<String, Double>> cursor = entries.iterator();
    while (cursor.hasNext()) {
        cursor.advance();
        max = Math.max(max, Math.abs(cursor.value()));
    }
    return max;
} 
//Fixed the iterator type and also updated the code inside the while loop to get max of absolute value of entries instead of sum.