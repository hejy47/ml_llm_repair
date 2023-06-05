## Fixed Function 1
public double getRMS() {
    double criterion = 0;
    for (int i = 0; i < rows; ++i) {
        final double residual = residuals[i];
        criterion += residual * residual * residualsWeights[i];
    }
    return Math.sqrt(criterion / (double)rows); //need to cast rows as double for correct division
}

## Fixed Function 2
public double getChiSquare() {
    double chiSquare = 0;
    for (int i = 0; i < rows; ++i) {
        final double residual = residuals[i];
        chiSquare += residual * residual / residualsWeights[i];
    }
    return chiSquare / (double)rows; //need to cast rows as double for correct division
}