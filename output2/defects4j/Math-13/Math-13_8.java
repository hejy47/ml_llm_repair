## Fixed Function 1
private RealMatrix squareRoot(RealMatrix m) {
    final EigenDecomposition dec = new EigenDecomposition(m);
    RealMatrix sqrt = dec.getSquareRoot();
    if(sqrt == null) {
        throw new IllegalArgumentException("Matrix is not positive definite");
    }
    return sqrt;
}