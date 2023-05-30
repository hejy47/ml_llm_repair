## Fixed Function 1
public double[] encode(final double[] x) {
    if (boundaries == null) {
        return x;
    }
    double[] res = new double[x.length];
    for (int i = 0; i < x.length; i++) {
        double diff = boundaries[1][i] - boundaries[0][i];
        res[i] = (x[i] - boundaries[0][i]) / diff;
        // Fix: Check if diff is 0 to avoid division by zero
        if (diff == 0) {
            res[i] = 0;
        }
    }
    return res;
}

## Fixed Function 2
public double[] decode(final double[] x) {
    if (boundaries == null) {
        return x;
    }
    double[] res = new double[x.length];
    for (int i = 0; i < x.length; i++) {
        double diff = boundaries[1][i] - boundaries[0][i];
        // Fix: Check if diff is 0 to avoid division by zero
        if (diff == 0) {
            res[i] = boundaries[0][i]; // Set to lower boundary value
        } else {
            res[i] = diff * x[i] + boundaries[0][i];
        }
    }
    return res;
}

## Fixed Function 3
public boolean isFeasible(final double[] x) {
    if (boundaries == null) {
        return true;
    }
    for (int i = 0; i < x.length; i++) {
        if (x[i] < boundaries[0][i]) { // Fix: Check if x is less than lower boundary
            return false;
        }
        if (x[i] > boundaries[1][i]) { // Fix: Check if x is greater than upper boundary
            return false;
        }
    }
    return true;
}