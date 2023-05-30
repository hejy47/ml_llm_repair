## Fixed Function 1
public double density(final double[] vals) throws DimensionMismatchException {
    final int dim = getDimension();
    if (vals.length != dim) {
        throw new DimensionMismatchException(dim, vals.length); // swap arguments
    }
    return FastMath.pow(2 * FastMath.PI, -dim / 2) * FastMath.pow(covarianceMatrixDeterminant, -0.5) * getExponentTerm(vals);
}