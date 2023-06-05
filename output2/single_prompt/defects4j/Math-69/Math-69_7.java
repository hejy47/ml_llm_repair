## Fixed Function 1
public RealMatrix getCorrelationPValues() throws MathException {
    TDistribution tDistribution = new TDistributionImpl(nObs - 2);
    int nVars = correlationMatrix.getColumnDimension();
    double[][] out = new double[nVars][nVars];
    for (int i = 0; i < nVars; i++) {
        for (int j = i + 1; j < nVars; j++) { // only iterate over the lower triangle
            double r = correlationMatrix.getEntry(i, j);
            double t = Math.abs(r * Math.sqrt((nObs - 2) / (1 - r * r)));
            out[i][j] = 2 * (1 - tDistribution.cumulativeProbability(t));
            out[j][i] = out[i][j]; // set the corresponding element in the upper triangle
        }
    }
    return new BlockRealMatrix(out);
}