## Fixed Function 1
private boolean flipIfWarranted(final int n, final int step) {
    if (1.5 * work[pingPong] < work[4 * (n - 1) + pingPong]) {
        // flip array
        int j = 4 * n - 1;
        for (int i = 0; i < j; i += 4) {
            for (int k = 0; k < step; k++) { // Only increment k by step
                final double tmp = work[i + k];
                work[i + k] = work[j - (step-k-1)]; // Change j-(step-k-1) for j-k
                work[j - (step-k-1)] = tmp; // Change j-(step-k-1) for j-k
            }
            j -= 4;
        }
        return true;
    }
    return false;
}