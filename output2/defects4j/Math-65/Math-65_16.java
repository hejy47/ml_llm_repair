## Fixed Function 1
public double getRMS() {
    double criterion = 0;
    for (int i = 0; i < rows; ++i) {
        final double residual = residuals[i];
        criterion += residual * residual * residualsWeights[i];
    }
    return Math.sqrt(criterion / (double)rows); // cast rows to double to ensure correct division
}

## Fixed Function 2
public double getChiSquare() {
    double chiSquare = 0;
    for (int i = 0; i < rows; ++i) {
        final double residual = residuals[i];
        chiSquare += residual * residual / Math.abs(residualsWeights[i]); // use absolute value to avoid negative weights
    }
    return chiSquare;
}