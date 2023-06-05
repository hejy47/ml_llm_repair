## Fixed Function 1
public double[] encode(final double[] x) {
    if (boundaries == null) {
        return x;
    }
    double[] res = new double[x.length];
    for (int i = 0; i < x.length; i++) {
        double diff = boundaries[1][i] - boundaries[0][i];
        res[i] = (x[i] - boundaries[0][i]) / diff;
        res[i] = Math.min(1.0, Math.max(0.0, res[i])); // add boundary check
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
        res[i] = diff * x[i] + boundaries[0][i];
    }
    return res;
}

## Fixed Function 3
public boolean isFeasible(final double[] x) {
    if (boundaries == null) {
        return true;
    }
    for (int i = 0; i < x.length; i++) {
        if (x[i] < boundaries[0][i]) { // check lower bound
            return false;
        }
        if (x[i] > boundaries[1][i]) { // check upper bound
            return false;
        }
    }
    return true;
}