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
            final RealPointValuePair expanded = evaluateNewSimplex(original, khi, comparator);
            if (comparator.compare(expanded, reflected) < 0) {
                // accept the expanded simplex
                simplex = expandedSimplex;
            } else {
                // accept the reflected simplex
                simplex = reflected;
            }
        } else {
            // try a contraction
            final RealPointValuePair contracted = evaluateNewSimplex(original, rho, comparator);
            if (comparator.compare(contracted, best) < 0) {
                // accept the contracted simplex
                simplex = contracted;
            } else {
                // if none of the above worked, perform a reduction
                final RealPointValuePair[] reducedSimplex = reduceSimplex(original, sigma);
                evaluateSimplex(reducedSimplex, comparator);
            }
        }
    }
}