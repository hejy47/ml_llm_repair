## Fixed Function 1
public double density(final double[] vals) throws DimensionMismatchException {
    final int dim = getDimension();
    if (vals.length != dim) {
        throw new DimensionMismatchException(vals.length, dim);
    }
    return FastMath.pow(2 * FastMath.PI, -dim / 2.0) * FastMath.pow(covarianceMatrixDeterminant, -0.5) * getExponentTerm(vals);
} 

(Note: The only change made was dividing by 2.0 instead of 2 to ensure that the exponent is a double.)