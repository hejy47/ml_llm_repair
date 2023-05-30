## Fixed Function 1
private RealMatrix squareRoot(RealMatrix m) {
    final EigenDecomposition dec = new EigenDecomposition(m);
    RealMatrix sqrtMatrix = dec.getSquareRoot();
    if(sqrtMatrix != null){
        return sqrtMatrix;
    } else {
        throw new IllegalArgumentException("Matrix is not positive definite");
    }
}