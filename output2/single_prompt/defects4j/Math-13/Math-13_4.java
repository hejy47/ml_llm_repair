## Fixed Function 1
private RealMatrix squareRoot(RealMatrix m) {
    final EigenDecomposition dec = new EigenDecomposition(m);
    return dec.getSquareRootMatrix();
}