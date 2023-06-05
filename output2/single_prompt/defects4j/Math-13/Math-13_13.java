## Fixed Function 1
private RealMatrix squareRoot(RealMatrix m) {
    final EigenDecomposition dec = new EigenDecomposition(m);
    RealMatrix result = dec.getV().multiply(dec.getD().sqrt()).multiply(dec.getV().transpose());
    return result;
}