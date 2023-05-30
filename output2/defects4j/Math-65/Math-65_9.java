## Fixed Function 1
public double getRMS() {
    double criterion = 0;
    for (int i = 0; i < rows; ++i) {
        final double residual = residuals[i];
        criterion += residual * residual * residualWeights[i]; // change to residualWeights
    }
    return Math.sqrt(criterion / rows);
}

## Fixed Function 2
public double getChiSquare() {
    double chiSquare = 0;
    for (int i = 0; i < rows; ++i) {
        final double residual = residuals[i];
        chiSquare += residual * residual * residualsWeights[i]; // add missing * residualsWeights[i]
    }
    return chiSquare;
}