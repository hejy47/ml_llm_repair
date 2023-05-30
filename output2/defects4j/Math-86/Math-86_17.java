## Fixed Function 1
public CholeskyDecompositionImpl(final RealMatrix matrix, final double relativeSymmetryThreshold, final double absolutePositivityThreshold) throws NonSquareMatrixException, NotSymmetricMatrixException, NotPositiveDefiniteMatrixException {
    if (!matrix.isSquare()) {
        throw new NonSquareMatrixException(matrix.getRowDimension(), matrix.getColumnDimension());
    }
    final int order = matrix.getRowDimension();
    lTData = new double[order][order];
    for (int i = 0; i < order; i++) {
        lTData[i] = matrix.getRow(i);
    }
    cachedL = null;
    cachedLT = null;
    // check the matrix before transformation
    for (int i = 0; i < order; ++i) {
        final double[] lI = lTData[i];
        if (lTData[i][i] < absolutePositivityThreshold) {
            throw new NotPositiveDefiniteMatrixException();
        }
        // check off-diagonal elements (and reset them to 0)
        for (int j = i + 1; j < order; ++j) {
            final double[] lJ = lTData[j];
            final double lIJ = lI[j];
            final double lJI = lJ[i];
            final double maxDelta = relativeSymmetryThreshold * Math.max(Math.abs(lIJ), Math.abs(lJI));
            if (Math.abs(lIJ - lJI) > maxDelta) {
                throw new NotSymmetricMatrixException();
            }
            lJ[i] = lJI / lI[i];
        }
    }
    // transform the matrix
    for (int i = 0; i < order; ++i) {
        final double[] ltI = lTData[i];
        // check diagonal element
        double sqrt = Math.sqrt(ltI[i]);
        if(sqrt == 0){
            throw new NotPositiveDefiniteMatrixException();
        }
        ltI[i] = sqrt;
        final double inverse = 1.0 / sqrt;
        for (int q = i+1; q < order; ++q) {
            ltI[q] *= inverse;
        }
        for (int q = i+1; q < order; ++q) {
            final double[] ltQ = lTData[q];
            for (int p = q; p < order; ++p) {
                ltQ[p] -= ltI[q] * ltI[p];
            }
        }
    }
}