## Fixed Function 1
public double[][] getCovariances(EstimationProblem problem) throws EstimationException {
    // set up the jacobian
    updateJacobian();
    // compute transpose(J).J, avoiding building big intermediate matrices
    final int rows = problem.getMeasurements().length;
    final int cols = problem.getAllParameters().length;
    final int max = cols * rows;
    double[][] jTj = new double[cols][cols];
    for (int i = 0; i < cols; ++i) {
        for (int j = i; j < cols; ++j) {
            double sum = 0;
            for (int k = 0; k < max; k += cols) {
                sum += jacobian[k + i] * jacobian[k + j];
            }
            jTj[i][j] = sum;
            jTj[j][i] = sum;
        }
    }
    RealMatrix tmatrix = new Array2DRowRealMatrix(jTj, false);
    try {
        // compute the covariances matrix
        return new LUDecomposition(tmatrix).getSolver().getInverse().getData();
    } catch (SingularMatrixException sme) {
        throw new EstimationException("unable to compute covariances: singular problem", new Object[0]);
    }
}

## Fixed Function 2
public double[] guessParametersErrors(EstimationProblem problem) throws EstimationException {
    int m = problem.getMeasurements().length;
    int p = problem.getAllParameters().length;
    if (m <= p) {
        throw new EstimationException("no degrees of freedom ({0} measurements, {1} parameters)", new Object[] { new Integer(m), new Integer(p) });
    }
    double[] errors = new double[problem.getAllParameters().length];
    final double c = Math.sqrt(getChiSquare(problem) / (m - p));
    double[][] covar = getCovariances(problem);
    RealMatrix cMatrix = new Array2DRowRealMatrix(covar,false);
    for (int i = 0; i < errors.length; ++i) {
        errors[i] = Math.sqrt(cMatrix.getEntry(i,i)) * c;
    }
    return errors;
}