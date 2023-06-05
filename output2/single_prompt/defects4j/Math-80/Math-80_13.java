## Fixed Function 1
private boolean flipIfWarranted(final int n, final int step) {
    double[] currentWork = work[pingPong];
    double[] newWork = work[1 - pingPong];
    if (1.5 * currentWork[n - 1] < currentWork[4 * n - 1]) {
        // flip array
        int j = 4 * n - 1;
        for (int i = 0; i <= j; i += 4) {
            for (int k = 0; k < 4; k += step) {
                final double tmp = currentWork[i + k];
                newWork[j - i + k] = tmp;
            }
        }
        work[pingPong] = newWork;
        work[1 - pingPong] = currentWork;
        pingPong = 1 - pingPong;
        return true;
    }
    return false;
}