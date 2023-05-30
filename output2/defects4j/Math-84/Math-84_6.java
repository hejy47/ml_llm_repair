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
            // compute the expanded simplex and check if it's better than the reflected simplex
            final RealPointValuePair expanded = evaluateNewSimplex(original, khi, comparator);
            if (comparator.compare(expanded, reflected) < 0) {
                // accept the expanded simplex
                simplex = simplex(extracted, reflected, expanded);
            }
            else {
                // accept the reflected simplex
                simplex = simplex(extracted, reflected);
            }
            return;
        }
        // compute the contracted simplex
        final RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
        if (comparator.compare(contracted, best) < 0) {
            // accept the contracted simplex
            simplex = simplex(extracted, contracted);
            // check convergence
            // ...
            return;
        }
    }
}

// helper methods to construct new simplices
private static RealPointValuePair[] simplex(RealPointValuePair[] vertices, RealPointValuePair... newVertices) {
    RealPointValuePair[] newSimplex = Arrays.copyOf(vertices, vertices.length);
    for (int i = 0; i < newVertices.length; ++i) {
        newSimplex[i+1] = newVertices[i];
    }
    return newSimplex;
}