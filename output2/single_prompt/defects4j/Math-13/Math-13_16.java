## Fixed Function 1
private RealMatrix squareRoot(RealMatrix m) {
    final EigenDecomposition dec = new EigenDecomposition(m);
    RealMatrix squareRootMatrix = dec.getSquareRoot();
    if (squareRootMatrix == null) {
        throw new IllegalArgumentException("Matrix cannot be square rooted.");
    }
    return squareRootMatrix;
}