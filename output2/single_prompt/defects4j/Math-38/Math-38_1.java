// altmov
// ----------------------------------------------------------------------------------------
private void prelim(double[] lowerBound, double[] upperBound) {
    printMethod();
    final int n = currentBest.getDimension();
    final int npt = numberOfInterpolationPoints;
    final int ndim = bMatrix.getRowDimension();
    final double rhosq = initialTrustRegionRadius * initialTrustRegionRadius;
    final double recip = 1d / rhosq;
    final int np = n + 1;
    // Set XBASE to the initial vector of variables, and set the initial
    // elements of XPT, BMAT, HQ, PQ and ZMAT to zero.
    for (int j = 0; j < n; j++) {
        originShift.setEntry(j, currentBest.getEntry(j));
        for (int k = 0; k < npt; k++) {
            interpolationPoints.setEntry(k, j, ZERO);
        }
        for (int i = 0; i < ndim; i++) {
            bMatrix.setEntry(i, j, ZERO);
        }
    }
    for (int i = 0, max = n * np / 2; i < max; i++) {
        modelSecondDerivativesValues.setEntry(i, ZERO);
    }
    for (int k = 0; k < npt; k++) {
        modelSecondDerivativesParameters.setEntry(k, ZERO);
        for (int j = 0, max = npt - np; j < max; j++) {
            zMatrix.setEntry(k, j, ZERO);
        }
    }

    int lastInterpolationPointIndex = -1;
    // Begin the initialization procedure. NF becomes one more than the number
    // of function values so far. The coordinates of the displacement of the
    // next initial interpolation point from XBASE are set in XPT(NF+1,.).
    for (int currentInterpolationPointIndex = 0; currentInterpolationPointIndex < npt; currentInterpolationPointIndex++) {
        final int nfm = currentInterpolationPointIndex + 1;
        if (nfm == 1) { // We generate X_1 first.
            // The trust-region center is X_1.
            trustRegionCenterInterpolationPointIndex = 0;
            // Initial step is always the initial trust-region radius.
            double initialStepSize = initialTrustRegionRadius;
            // Find the index of the first non-fixed variable.
            int freeVariableIndex = -1;
            do {
                freeVariableIndex++;
            } while (freeVariableIndex < n && (lowerBound[freeVariableIndex] == upperBound[freeVariableIndex]));
            if (freeVariableIndex == n) {
                throw new MoreVariablesThanEquationsException();
            }
            // Calculate the initial point X_1.
            interpolationPoints.setEntry(nfm, freeVariableIndex, initialStepSize);
        } else { // We generate X_{nfm} for \geq 2.
            // Update the last interpolation point to X_{nfm-1}.
            for (int j = 0; j < n; j++) {
                interpolationPoints.setEntry(lastInterpolationPointIndex, j, currentBest.getEntry(j));
            }
            double objectiveValueAtLastInterpolationPoint = computeObjectiveValue(currentBest.toArray());
            fAtInterpolationPoints.setEntry(lastInterpolationPointIndex, isMinimize ? objectiveValueAtLastInterpolationPoint : -objectiveValueAtLastInterpolationPoint);
            double fAtLastInterpolationPoint = fAtInterpolationPoints.getEntry(lastInterpolationPointIndex);
            // If we have generated fewer points than n+1, we simply generate the
            // next point according to the fixed pattern.
            if (nfm <= n + 1) {
                int j = nfm - 2;
                int sign = (j % 2 == 0) ? 1 : -1;
                j /= 2;
                int currentInterpolationPointIndex0 = currentInterpolationPointIndex - n;
                final int index1 = trustRegionCenterInterpolationPointIndex;
                final double deltaX1 = interpolationPoints.getEntry(currentInterpolationPointIndex0, index1);
                final double x1 = currentBest.getEntry(index1);
                final double lowerBound1 = lowerBound[index1];
                final double upperBound1 = upperBound[index1];
                final double currentPointComponent = x1;
                final double newPointComponent = Math.min(Math.max(lowerBound1, x1 + sign * deltaX1), upperBound1);
                final double deltaXDiff = newPointComponent - currentPointComponent;
                interpolationPoints.setEntry(nfm, index1, deltaXDiff);
                for (int i = 0; i < j; i++) {
                    final int indexI = indices[i];
                    final double deltaXI = coordinateDifference.getEntry(i);
                    final double xI = currentBest.getEntry(indexI);
                    final double lowerBoundI = lowerBound[indexI];
                    final double upperBoundI = upperBound[indexI];
                    final double currentPointComponentI = xI;
                    final double newPointComponentI = Math.min(Math.max(lowerBoundI, xI + sign * deltaXI), upperBoundI);
                    final double deltaXDiffI = newPointComponentI - currentPointComponentI;
                    interpolationPoints.setEntry(nfm, indexI, deltaXDiffI);
                }
            } else {
                // We generate X_{nfm} using quadratic model.
                trustRegionCenterInterpolationPointIndex = 0;
                lastInterpolationPointIndex = currentInterpolationPointIndex - 1;
                update(bMatrix, zMatrix, modelSecondDerivativesValues, interpolationPoints, fAtInterpolationPoints, trustRegionCenterInterpolationPointIndex, currentInterpolationPointIndex, interpolateEndpoints);
                for (int j = 0; j < n; j++) {
                    currentBest.setEntry(j, Math.min(Math.max(lowerBound[j], originShift.getEntry(j) + interpolationPoints.getEntry(currentInterpolationPointIndex, j)), upperBound[j]));
                    if (interpolationPoints.getEntry(currentInterpolationPointIndex, j) == lowerDifference.getEntry(j)) {
                        currentBest.setEntry(j, lowerBound[j]);
                    }
                    if (interpolationPoints.getEntry(currentInterpolationPointIndex, j) == upperDifference.getEntry(j)) {
                        currentBest.setEntry(j, upperBound[j]);
                    }
                }
                final double objectiveValue = computeObjectiveValue(currentBest.toArray());
                final double f = isMinimize ? objectiveValue : -objectiveValue;
                fAtInterpolationPoints.setEntry(currentInterpolationPointIndex, f);
                if (f < fAtInterpolationPoints.getEntry(trustRegionCenterInterpolationPointIndex)) {
                    trustRegionCenterInterpolationPointIndex = currentInterpolationPointIndex;
                }
            }
        }
        lastInterpolationPointIndex = currentInterpolationPointIndex;
    }
}