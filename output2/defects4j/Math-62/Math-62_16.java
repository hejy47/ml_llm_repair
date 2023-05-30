## Fixed Function 1
public UnivariateRealPointValuePair optimize(final FUNC f, final GoalType goal, final double min, final double max) throws FunctionEvaluationException {
    return optimize(f, goal, min, max, (min + max) / 2); // Set startValue to the midpoint of the interval
}

## Fixed Function 2
public UnivariateRealPointValuePair optimize(final FUNC f, final GoalType goal, final double min, final double max, final double startValue) throws FunctionEvaluationException {
    optima = new UnivariateRealPointValuePair[starts];
    totalEvaluations = 0;
    // Multi-start loop.
    for (int i = 0; i < starts; ++i) {
        try {
            final double bound1 = (i == 0) ? min : min + generator.nextDouble() * (max - min);
            final double bound2 = (i == 0) ? max : min + generator.nextDouble() * (max - min);
            optima[i] = optimizer.optimize(f, goal, FastMath.min(bound1, bound2), FastMath.max(bound1, bound2), startValue); // Pass startValue to optimizer
        } catch (FunctionEvaluationException fee) {
            optima[i] = null;
        } catch (ConvergenceException ce) {
            optima[i] = null;
        }
        final int usedEvaluations = optimizer.getEvaluations();
        optimizer.setMaxEvaluations(optimizer.getMaxEvaluations() - usedEvaluations);
        totalEvaluations += usedEvaluations;
    }
    sortPairs(goal);
    if (optima[0] == null) {
        throw new ConvergenceException(LocalizedFormats.NO_CONVERGENCE_WITH_ANY_START_POINT, starts);
    }
    // Return the point with the best objective function value.
    return optima[0];
}