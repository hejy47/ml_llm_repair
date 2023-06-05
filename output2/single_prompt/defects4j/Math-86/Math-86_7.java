## Fixed Function 1
public CholeskyDecompositionImpl(final RealMatrix matrix, final double relativeSymmetryThreshold, final double absolutePositivityThreshold) throws NonSquareMatrixException, NotSymmetricMatrixException, NotPositiveDefiniteMatrixException {
    if (!matrix.isSquare()) {
        throw new NonSquareMatrixException(matrix.getRowDimension(), matrix.getColumnDimension());
    }
    final int order = matrix.getRowDimension();
    lTData = new double[order][order]; // create new array to avoid modifying the original matrix
    for (int i = 0; i < order; ++i) {
        System.arraycopy(matrix.getRow(i), 0, lTData[i], 0, order); // copy data from matrix
    }
    cachedL = null;
    cachedLT = null;
    // check the matrix before transformation
    for (int i = 0; i < order; ++i) {
        final double[] lI = lTData[i];
        if (lI[i] < absolutePositivityThreshold) {
            throw new NotPositiveDefiniteMatrixException();
        }
        // check off-diagonal elements (and reset them to 0)
        for (int j = i + 1; j < order; ++j) {
            final double[] lJ = lTData[j];
            final double lIJ = lTData[i][j];
            final double lJI = lJ[i];
            final double maxDelta = relativeSymmetryThreshold * Math.max(Math.abs(lIJ), Math.abs(lJI));
            if (Math.abs(lIJ - lJI) > maxDelta) {
                throw new NotSymmetricMatrixException();
            }
            lJ[i] = 0;
        }
    }
    // transform the matrix
    for (int i = 0; i < order; ++i) {
        final double[] ltI = lTData[i];
        // check diagonal element
        ltI[i] = Math.sqrt(ltI[i]);
        final double inverse = 1.0 / ltI[i];
        for (int q = order - 1; q > i; --q) {
            ltI[q] *= inverse;
            final double[] ltQ = lTData[q];
            for (int p = q; p < order; ++p) {
                ltQ[p] -= ltI[q] * ltI[p];
            }
        }
    }
}