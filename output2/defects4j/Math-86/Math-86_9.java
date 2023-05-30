## Fixed Function 1
public CholeskyDecompositionImpl(final RealMatrix matrix, final double relativeSymmetryThreshold, final double absolutePositivityThreshold) throws NonSquareMatrixException, NotSymmetricMatrixException, NotPositiveDefiniteMatrixException {
    if (!matrix.isSquare()) {
        throw new NonSquareMatrixException(matrix.getRowDimension(), matrix.getColumnDimension());
    }
    final int order = matrix.getRowDimension();
    lTData = matrix.getData();
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
            lJ[i] = lI[j] = (lI[j] + lJ[i]) / 2.0; // fix off-diagonal elements
        }
    }
    // transform the matrix
    for (int i = 0; i < order; ++i) {
        final double[] ltI = lTData[i];
        // check diagonal element
        for (int j = 0; j < i; ++j) {
            ltI[i] -= ltI[j] * ltI[j];
        }
        if (ltI[i] < 0) {
            throw new NotPositiveDefiniteMatrixException(); // fix negative value on diagonal element
        }
        ltI[i] = Math.sqrt(ltI[i]);
        final double inverse = 1.0 / ltI[i];
        for (int q = i + 1; q < order; ++q) {
            final double[] ltQ = lTData[q];
            for (int p = i + 1; p < order; ++p) {
                ltQ[p] -= ltI[i] * ltI[q];
            }
            ltQ[i] = (ltQ[i] - ltI[i] * ltQ[q]) * inverse;
        }
        ltI[i] *= inverse;
    }
}