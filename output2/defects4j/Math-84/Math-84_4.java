## Fixed Function 1
@Override
protected void iterateSimplex(final Comparator<RealPointValuePair> comparator) throws FunctionEvaluationException, OptimizationException, IllegalArgumentException {
    while (true) {
        incrementIterationsCounter();
        // save the original vertex
        final RealPointValuePair[] original = simplex;
        final RealPointValuePair best = original[0];
        // perform a reflection step
        final RealPointValuePair reflected = evaluateNewSimplex(original, 1.0, comparator);
        if (comparator.compare(reflected, best) < 0) {
            // compute the expanded simplex
            final RealPointValuePair[] reflectedSimplex = simplex;
            final RealPointValuePair expanded = evaluateNewSimplex(reflectedSimplex, khi, comparator);
            if (comparator.compare(expanded, reflected) < 0) {
                // accept the expanded simplex
                simplex = expandedSimplex;
            } else {
                // accept the reflected simplex
                simplex = reflectedSimplex;
            }
            return;
        }
        // compute the contracted simplex
        final RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
        if (comparator.compare(contracted, best) < 0) {
            // accept the contracted simplex
            simplex = contracted;
        } else {
            // check convergence
            boolean isConverged = true;
            for (int i = 1; i < simplex.length; i++) {
                if (comparator.compare(simplex[i], simplex[0]) != 0) {
                    isConverged = false;
                    break;
                }
            }
            if (isConverged) {
                return;
            }
        }
    }
}