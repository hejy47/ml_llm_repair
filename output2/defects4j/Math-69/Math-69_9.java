## Fixed Function 1
public RealMatrix getCorrelationPValues() throws MathException {
    TDistribution tDistribution = new TDistributionImpl(nObs - 2);
    int nVars = correlationMatrix.getColumnDimension();
    double[][] out = new double[nVars][nVars];
    for (int i = 0; i < nVars; i++) {
        for (int j = 0; j < nVars; j++) {
            if (i == j) {
                out[i][j] = 0d;
            } else {
                double r = correlationMatrix.getEntry(i, j);
                double t = Math.abs(r * Math.sqrt((nObs - 2) / (1 - r * r)));
                double p = 2 * (1 - tDistribution.cumulativeProbability(t));
                // fix to avoid NaN values
                if (Double.isNaN(p)) {
                    p = 1d;
                }
                out[i][j] = p;
            }
        }
    }
    return new BlockRealMatrix(out);
}