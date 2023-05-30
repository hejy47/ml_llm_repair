## Fixed Function 1
private RealMatrix squareRoot(RealMatrix m) {
    final EigenDecomposition dec = new EigenDecomposition(m);
    final RealMatrix sqrtMatrix = dec.getSquareRoot();
    if(sqrtMatrix == null) {
        throw new IllegalArgumentException("Matrix is not positive definite");
    }
    return sqrtMatrix;
}