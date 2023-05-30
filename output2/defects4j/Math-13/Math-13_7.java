## Fixed Function 1
private RealMatrix squareRoot(RealMatrix m) {
    final EigenDecomposition dec = new EigenDecomposition(m);
    final RealMatrix sqrtMatrix = dec.getSquareRoot();
    for (int i = 0; i < sqrtMatrix.getRowDimension(); i++) {
        for (int j = 0; j < sqrtMatrix.getColumnDimension(); j++) {
            if (sqrtMatrix.getEntry(i, j) < 0) {
                throw new IllegalArgumentException("Matrix square root of negative matrix is not supported");
            }
        }
    }
    return sqrtMatrix;
}