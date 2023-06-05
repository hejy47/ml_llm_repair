## Fixed Function 1
private RealMatrix squareRoot(RealMatrix m) {
    final EigenDecomposition dec = new EigenDecomposition(m);
    final RealMatrix sqrt = dec.getSquareRoot();
    if (sqrt == null) {
        throw new ArithmeticException("Matrix is not positive definite");
    }
    return sqrt;
}