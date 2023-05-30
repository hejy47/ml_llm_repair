## Fixed Function 1
public double density(final double[] vals) throws DimensionMismatchException {
    final int dim = getDimension();
    if (vals.length != dim) {
        throw new DimensionMismatchException(vals.length, dim);
    }
    double determinant = getCovarianceMatrix().determinant();  // Update covariance matrix determinant
    return FastMath.pow(2 * FastMath.PI, -dim / 2) * FastMath.pow(determinant, -0.5) * getExponentTerm(vals);
}