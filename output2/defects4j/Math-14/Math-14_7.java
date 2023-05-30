## Fixed Function 1
private RealMatrix squareRoot(RealMatrix m) {
    RealMatrix result;
    try {
        final EigenDecomposition dec = new EigenDecomposition(m);
        result = dec.getSquareRoot();
    } catch (NotPositiveDefiniteMatrixException ex) {
        result = new LUDecomposition(m).getSolver().getInverse();
        result = result.multiply(result.transpose());
    }
    return result;
}

## Fixed Function 2
public Weight(double[] weight) {
    final int dim = weight.length;
    weightMatrix = MatrixUtils.createRealDiagonalMatrix(weight);
}