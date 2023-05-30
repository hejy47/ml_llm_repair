## Fixed Function 1
public double solve(final UnivariateRealFunction f, final double min, final double max, final double initial) throws MaxIterationsExceededException, FunctionEvaluationException {
    clearResult();
    verifySequence(min, initial, max);
    // return the initial guess if it is good enough
    double yInitial = f.value(initial);
    if (Math.abs(yInitial) <= functionValueAccuracy) {
        setResult(initial, yInitial);
        return result;
    }
    // return the first endpoint if it is good enough
    double yMin = f.value(min);
    if (Math.abs(yMin) <= functionValueAccuracy) {
        setResult(min, yMin);
        return result;
    }
    // reduce interval if min and initial bracket the root
    if (yInitial * yMin < 0) {
        return solve(f, min, initial, yMin, yInitial);
    }
    // return the second endpoint if it is good enough
    double yMax = f.value(max);
    if (Math.abs(yMax) <= functionValueAccuracy) {
        setResult(max, yMax);
        return result;
    }
    // reduce interval if initial and max bracket the root
    if (yInitial * yMax < 0) {
        return solve(f, initial, max, yInitial, yMax);
    }
    // full Brent algorithm starting with provided initial guess
    return solve(f, min, max, initial, yInitial, max, yMax);
}

## Fixed Function 2
private double computeObjectiveValue(double[] objectiveValue, final double weight, final RealVector residuals) {
    residuals.walkInOptimizedOrder(new RealVectorPreservingVisitor() {

        public void visit(int index, double value) {
            double weightedResidual = weight * value;
            objectiveValue[0] += weightedResidual * weightedResidual / 2;
            objectiveValue[1] += Math.abs(weightedResidual);
            objectiveValue[2] = Math.max(objectiveValue[2], Math.abs(weightedResidual));
        }
    });
    return objectiveValue[0];
}

private double computeObjectiveValue(double[] objectiveValue, final double weight, final RealMatrix weightedJacobian, final RealVector residuals) {
    int nR = residuals.getDimension();
    int nC = weightedJacobian.getColumnDimension();
    double[] resWeights = new double[nR];
    for (int i = 0; i < nR; ++i) {
        double wi = FastMath.sqrt(weight * Math.abs(residuals.getEntry(i)));
        resWeights[i] = wi;
    }
    double obj = 0;
    for (int j = 0; j < nC; ++j) {
        double sum = 0;
        for (int i = 0; i < nR; ++i) {
            sum += weightedJacobian.getEntry(i, j) * resWeights[i];
        }
        double weightedResidual = sum;
        objectiveValue[1] += Math.abs(weightedResidual);
        objectiveValue[2] = Math.max(objectiveValue[2], Math.abs(weightedResidual));
        obj += weightedResidual * weightedResidual;
    }
    objectiveValue[0] += obj / 2;
    return objectiveValue[0];
}