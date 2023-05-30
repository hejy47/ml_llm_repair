## Fixed Function 1
public CholeskyDecompositionImpl(final RealMatrix matrix, final double relativeSymmetryThreshold, final double absolutePositivityThreshold) throws NonSquareMatrixException, NotSymmetricMatrixException, NotPositiveDefiniteMatrixException {
    if (!matrix.isSquare()) {
        throw new NonSquareMatrixException(matrix.getRowDimension(), matrix.getColumnDimension());
    }
    final int order = matrix.getRowDimension();
    lTData = new double[order][order];
    matrix.walkInOptimizedOrder(new DefaultRealMatrixPreservingVisitor() {
        /** Check if the matrix is symmetric and extract the lower-triangular part. */
        @Override public void visit(int row, int column, double value) {
            if (column > row) {
                lTData[column][row] = value;
            } else {
                lTData[row][column] = value;
            }
        }
    });
    cachedL = null;
    cachedLT = null;
    // check the matrix before transformation
    for (int i = 0; i < order; ++i) {
        final double[] lI = lTData[i];
        if (lTData[i][i] < absolutePositivityThreshold) {
            throw new NotPositiveDefiniteMatrixException();
        }
        // check off-diagonal elements
        for (int j = i + 1; j < order; ++j) {
            final double[] lJ = lTData[j];
            final double lIJ = lI[j];
            final double lJI = lJ[i];
            final double maxDelta = relativeSymmetryThreshold * Math.max(Math.abs(lIJ), Math.abs(lJI));
            if (Math.abs(lIJ - lJI) > maxDelta) {
                throw new NotSymmetricMatrixException();
            }
        }
    }
    // transform the matrix
    for (int i = 0; i < order; ++i) {
        final double[] ltI = lTData[i];
        // check diagonal element
        double min = ltI[i];
        for (int j = 0; j < i; ++j) {
            final double[] ltJ = lTData[j];
            final double lJI = ltJ[i];
            min -= lJI * lJI * ltJ[j];
        }
        if (min <= 0) {
            throw new NotPositiveDefiniteMatrixException();
        }
        ltI[i] = Math.sqrt(min);
        final double inverse = 1.0 / ltI[i];
        for (int j = i + 1; j < order; ++j) {
            final double[] ltJ = lTData[j];
            double sum = ltJ[i];
            for (int k = 0; k < i; ++k) {
                sum -= ltI[k] * ltJ[k] * lTData[k][i];
            }
            ltJ[i] = sum * inverse;
        }
    }
}