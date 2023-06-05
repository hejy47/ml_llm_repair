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
                simplex = reflectedSimplex;
            } else {
                // accept the reflected simplex
                simplex = reflected;
            }
            return;
        }
        // compute the contracted simplex
        final RealPointValuePair contracted = evaluateNewSimplex(original, gamma, comparator);
        if (comparator.compare(contracted, best) < 0) {
            // accept the contracted simplex
            // check convergence
            simplex = contracted;
            return;
        } else {
            // perform a reduction step
            for (int i = 1; i < original.length; i++) {
                final double[] res = new double[original[i].getPointRef().length];
                for (int j = 0; j < res.length; j++) {
                    res[j] = rho * (original[i].getPointRef()[j] - best.getPointRef()[j]);
                    res[j] += best.getPointRef()[j];
                }
                original[i] = new RealPointValuePair(res, Double.NaN);
            }
        }
    }
}