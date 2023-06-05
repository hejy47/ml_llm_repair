## Fixed Function 1
private RealMatrix squareRoot(RealMatrix m) {
    final EigenDecomposition dec = new EigenDecomposition(m);
    final RealMatrix sqrt = dec.getSquareRoot();
    if (sqrt != null) {
        return sqrt;
    } else {
        throw new ArithmeticException("Matrix cannot be square rooted.");
    }
}