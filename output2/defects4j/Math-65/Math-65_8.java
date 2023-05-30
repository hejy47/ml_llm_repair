## Fixed Function 1
public double getRMS() {
    double criterion = 0;
    for (int i = 0; i < rows; ++i) {
        final double residual = residuals[i];
        criterion += residual * residual * residualWeights[i]; //fixed a typo in variable name
    }
    return Math.sqrt(criterion / rows);
}

## Fixed Function 2
public double getChiSquare() {
    double chiSquare = 0;
    for (int i = 0; i < rows; ++i) {
        final double residual = residuals[i];
        chiSquare += residual * residual / (residualsWeights[i] * residualsWeights[i]); //fixed the division by adding the square of the residualWeights
    }
    return chiSquare;
}