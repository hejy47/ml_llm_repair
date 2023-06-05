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
            final RealPointValuePair[] expandedSimplex = simplex;
            final RealPointValuePair expanded = evaluateNewSimplex(original, rho, comparator);
            if (comparator.compare(expanded, reflected) < 0) {
                // accept the expanded simplex
                simplex = expandedSimplex;
            } else {
                // accept the reflected simplex
                simplex = reflected;
            }
        } else {
            // perform a contraction step
            final RealPointValuePair contracted = evaluateNewSimplex(original, -sigma, comparator);
            if (comparator.compare(contracted, best) < 0) {
                // accept the contracted simplex
                simplex = contracted;
            } else {
                // perform a shrink step
                for (int i = 1; i < simplex.length; i++) {
                    final RealPointValuePair[] shrunkSimplex = simplex.clone();
                    final RealPointValuePair vertex = shrunkSimplex[i];
                    shrunkSimplex[i] = new RealPointValuePair(getContractedPoint(original, vertex.getPoint(), omega), Double.NaN);
                    simplex = evaluateSimplex(shrunkSimplex, comparator);
                }
            }
        }
    }
}