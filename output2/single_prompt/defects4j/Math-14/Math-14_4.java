## Fixed Function 1
private RealMatrix squareRoot(RealMatrix m) {
    final EigenDecomposition dec = new EigenDecomposition(m);
    return dec.getSquareRootMatrix();
}

## Fixed Function 2
public Weight(double[] weight) {
    final int dim = weight.length;
    weightMatrix = org.apache.commons.math3.linear.MatrixUtils.createRealIdentityMatrix(dim);
    for (int i = 0; i < dim; i++) {
        weightMatrix.setEntry(i, i, weight[i]);
    }
}