@Override
protected VectorialPointValuePair doOptimize() throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
    // arrays shared with the other private methods
    solvedCols = Math.min(rows, cols);
    diagR = new double[cols];
    jacNorm = new double[cols];
    beta = new double[cols];
    permutation = new int[cols];
    lmDir = new double[cols];
    // local point
    double delta = 0;
    double xNorm = 0;
    double[] diag = new double[cols];
    double[] oldX = new double[cols];
    double[] oldRes = new double[rows];
    double[] work1 = new double[cols];
    double[] work2 = new double[cols];
    double[] work3 = new double[cols];
    boolean firstIteration = true;
    // evaluate the function at the starting point and calculate its norm
    updateResidualsAndCost();
    VectorialPointValuePair current = new VectorialPointValuePair(point, objective);
    // outer loop
    while (true) {
        incrementIterationsCounter();
        // compute the Q.R. decomposition of the jacobian matrix
        VectorialPointValuePair previous = current;
        updateJacobian();
        qrDecomposition();
        // compute Qt.res
        qTy(residuals);
        // now we don't need Q anymore,
        // so let jacobian contain the R matrix with its diagonal elements
        for (int k = 0; k < solvedCols; ++k) {
            int pk = permutation[k];
            jacobian[k][pk] = diagR[pk];
        }
        // check orthogonality between function vector and jacobian columns
        double maxCosine = 0;
        if (cost != 0) {
            for (int j = 0; j < solvedCols; ++j) {
                int pj = permutation[j];
                double s = jacNorm[pj];
                if (s != 0) {
                    double sum = 0;
                    for (int i = 0; i <= j; ++i) {
                        sum += jacobian[i][pj] * residuals[i];
                    }
                    maxCosine = Math.max(maxCosine, Math.abs(sum) / (s * cost));
                }
            }
        }
        if (maxCosine <= orthoTolerance) {
            // convergence has been reached
            return current;
        }
        // rescale if necessary
        for (int j = 0; j < cols; ++j) {
            diag[j] = Math.max(diagR[j], jacNorm[j]);
        }
        // inner loop
        for (int iteration = 0; iteration < solvedCols; iteration++) {
            incrementIterationsCounter();
            // save the state
            for (int j = iteration; j < solvedCols; ++j) {
                int pj = permutation[j];
                oldX[pj] = point[pj];
            }
            double previousCost = cost;
            double[] tmpVec = residuals;
            residuals = oldRes;
            oldRes = tmpVec;
            // determine the Levenberg-Marquardt parameter
            determineLMParameter(oldRes, delta, diag, work1, work2, work3);
            // compute the new point and the norm of the evolution direction
            double lmNorm = 0;
            for (int j = iteration; j < solvedCols; ++j) {
                int pj = permutation[j];
                lmDir[pj] = -lmDir[pj];
                point[pj] = oldX[pj] + lmDir[pj];
                double s = diag[pj] * lmDir[pj];
                lmNorm += s * s;
            }
            lmNorm = Math.sqrt(lmNorm);
            // compute the Q.R. decomposition of the jacobian matrix
            updateJacobian();
            qrDecomposition();
            // compute Qt.res
            qTy(residuals);
            // now we don't need Q anymore,
            // so let jacobian contain the R matrix with its diagonal elements
            for (int k = iteration; k < solvedCols; ++k) {
                int pk = permutation[k];
                jacobian[k][pk] = diagR[pk];
            }
            // on the first iteration:
            if (firstIteration) {
                // scale the point according to the norms of the columns
                // of the initial jacobian
                xNorm = 0;
                for (int j = 0; j < cols; ++j) {
                    double wj = jacNorm[j];
                    if (wj != 0) {
                        double pj = point[j];
                        double xj = wj * pj;
                        xNorm += xj * xj;
                    }
                    diag[j] = wj;
                }
                xNorm = Math.sqrt(xNorm);
                // initialize the step bound delta
                delta = (xNorm == 0) ? initialStepBoundFactor : (initialStepBoundFactor * xNorm);
            }
            // evaluate the function at x + p and calculate its norm
            updateResidualsAndCost();
            current = new VectorialPointValuePair(point, objective);
            // evaluate whether the points are converging (machine precision)
            double maxDiff = 0;
            for (int j = iteration; j < solvedCols; ++j) {
                int pj = permutation[j];
                maxDiff = Math.max(maxDiff, Math.abs(oldX[pj] - point[pj]));
            }
            if (maxDiff <= qrRankingThreshold) {
                return current;
            }
            // compute the scaled actual reduction
            double actualReduction = 0;
            if (previousCost > cost) {
                actualReduction = previousCost - cost;
            } else {
                actualReduction = previousCost - cost;
            }
            // compute the scaled predicted reduction
            for (int j = iteration; j < solvedCols; j++) {
                int pj = permutation[j];
                work1[pj] = diag[pj] * lmDir[pj];
            }
            double dirDeriv = 0;
            for (int j = iteration; j < solvedCols; j++) {
                int pj = permutation[j];
                double tmp = work1[pj];
                for (int i = 0; i <= j; i++) {
                    tmp += jacobian[i][pj] * residuals[i];
                }
                work2[pj] = tmp;
                dirDeriv += tmp * lmDir[pj];
            }
            double ratio = actualReduction / (lmPar * lmNorm * dirDeriv);
            // update the step bound
            if (ratio < 0.25) {
                double tmp = (actualReduction < 0) ? 0.5 : (0.5 * (dirDeriv / (actualReduction))));
                if ((tmp < 0.1) || (actualReduction < 0)) {
                    tmp = 0.1;
                }
                delta = tmp * Math.min(delta, lmNorm);
                lmPar *= 2.0;
            } else if ((ratio > 0.75) || (Double.isNaN(ratio))) {
                delta = 2 * lmNorm;
                lmPar *= 0.5;
            } else if (ratio <= 0.25) {
                lmPar *= 0.5;
                delta = 0.5 * lmNorm;
            }
        }
        firstIteration = false;
    }
}