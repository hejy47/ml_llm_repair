## Fixed Function 1
public RealMatrix getU() throws InvalidMatrixException {
    if (cachedU == null) {
        final int p = singularValues.length;
        if (m >= n) {
            // the tridiagonal matrix is Bt.B, where B is upper bidiagonal
            final RealMatrix e = eigenDecomposition.getV().getSubMatrix(0, p - 1, 0, p - 1);
            final double[][] eData = e.getData();
            final double[][] wData = new double[m][p];
            double[] ei1 = eData[0];
            for (int i = 0; i < p - 1; ++i) {
                // compute W = B.E.S^(-1) where E is the eigenvectors matrix
                final double mi = mainBidiagonal[i];
                final double[] ei0 = ei1;
                final double[] wi = wData[i];
                ei1 = eData[i + 1];
                final double si = secondaryBidiagonal[i];
                for (int j = 0; j < p; ++j) {
                    wi[j] = (mi * ei0[j] + si * ei1[j]) / singularValues[j];
                }
            }
            for (int j = 0; j < p; ++j) {
                wData[p - 1][j] = ei1[j] * mainBidiagonal[p - 1] / singularValues[j];
            }
            for (int i = p; i < m; ++i) {
                wData[i] = new double[p];
            }
            cachedU = transformer.getU().multiply(MatrixUtils.createRealMatrix(wData));
        } else {
            // the tridiagonal matrix is B.Bt, where B is lower bidiagonal
            final RealMatrix e = eigenDecomposition.getV().getSubMatrix(0, m - 1, 0, p - 1);
            cachedU = transformer.getU().multiply(e);
        }
    }
    // return the cached matrix
    return cachedU;
}

## Fixed Function 2
public RealMatrix getV() throws InvalidMatrixException {
    if (cachedV == null) {
        final int p = singularValues.length;
        if (m >= n) {
            // the tridiagonal matrix is Bt.B, where B is upper bidiagonal
            final RealMatrix e = eigenDecomposition.getV().getSubMatrix(0, p - 1, 0, n - 1);
            cachedV = transformer.getV().multiply(e.transpose());
        } else {
            // the tridiagonal matrix is B.Bt, where B is lower bidiagonal
            // compute W = Bt.E.S^(-1) where E is the eigenvectors matrix
            final RealMatrix e = eigenDecomposition.getV().getSubMatrix(0, p - 1, 0, p - 1);
            final double[][] eData = e.getData();
            final double[][] wData = new double[n][p];
            double[] ei1 = eData[0];
            for (int i = 0; i < p - 1; ++i) {
                final double mi = mainBidiagonal[i];
                final double[] ei0 = ei1;
                final double[] wi = wData[i];
                ei1 = eData[i + 1];
                final double si = secondaryBidiagonal[i];
                for (int j = 0; j < p; ++j) {
                    wi[j] = (mi * ei0[j] + si * ei1[j]) / singularValues[j];
                }
            }
            for (int j = 0; j < p; ++j) {
                wData[p - 1][j] = ei1[j] * mainBidiagonal[p - 1] / singularValues[j];
            }
            for (int i = p; i < n; ++i) {
                wData[i] = new double[p];
            }
            cachedV = transformer.getV().multiply(MatrixUtils.createRealMatrix(wData)).transpose();
        }
    }
    // return the cached matrix
    return cachedV;
}