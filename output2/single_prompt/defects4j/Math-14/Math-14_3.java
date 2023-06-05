## Fixed Function 1
private RealMatrix squareRoot(RealMatrix m) {
    final EigenDecomposition dec = new EigenDecomposition(m);
    return dec.getSquareRootMatrix();
}

## Fixed Function 2
public Weight(double[] weight) {
    final int dim = weight.length;
    weightMatrix = org.apache.commons.math3.linear.MatrixUtils.createRealDiagonalMatrix(weight);
}