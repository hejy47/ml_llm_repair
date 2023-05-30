## Fixed Function 1
public double getRMS() {
    double criterion = 0;
    for (int i = 0; i < rows; ++i) {
        final double residual = residuals[i];
        criterion += residual * residual * residualsWeights[i];
    }
    if(rows > 0) {
        return Math.sqrt(criterion / rows);
    }
    return 0.0;
}

## Fixed Function 2
public double getChiSquare() {
    double chiSquare = 0;
    for (int i = 0; i < rows; ++i) {
        final double residual = residuals[i];
        if(residualsWeights[i] > 0) {
            chiSquare += residual * residual / residualsWeights[i];
        }
    }
    return chiSquare;
}