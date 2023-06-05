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
            final RealPointValuePair[] reflectedSimplex = simplex.clone(); //clone the simplex
            simplex = reflectedSimplex; //save the reflected simplex
            final RealPointValuePair expanded = evaluateNewSimplex(reflectedSimplex, khi, comparator); //evaluate on reflected simplex
            if (comparator.compare(expanded, reflected) < 0) { //compare with reflected, not original
                simplex = expanded; //accept the expanded simplex
            } else {
                simplex = reflected; //reject the expanded simplex, save the reflected one
            }
        } else { //if reflected is not better than best
            // perform a contraction step
            final RealPointValuePair contracted = evaluateNewSimplex(original, delta, comparator);
            if (comparator.compare(contracted, best) < 0) {
                // accept the contracted simplex
                simplex = contracted;
            } else {
                // perform a shrink step
                final RealPointValuePair[] shrinkedSimplex = contractSimplex(original, shrink);
                simplex = evaluateNewSimplex(shrinkedSimplex, 1.0, comparator);
                if (comparator.compare(simplex, best) >= 0) { //if the new simplex is not better than the best
                    return; //stop, we have converged
                }
            }
        }
    }
}