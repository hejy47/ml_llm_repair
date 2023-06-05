## Fixed Function 1
private Integer getBasicRow(final int col, boolean ignoreObjectiveRows) {
    Integer row = null;
    int start = ignoreObjectiveRows ? 1 : 0;
    for (int i = start; i < getHeight(); i++) {
        if (MathUtils.equals(getEntry(i, col), 1.0, epsilon) && row == null) {
            row = i;
        } else if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            return null;
        }
    }
    return row;
}

## Fixed Function 2
protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer negativeVarBasicRow = getBasicRow(getNegativeDecisionVariableOffset(), false);
    double mostNegative = negativeVarBasicRow == null ? 0 : getEntry(negativeVarBasicRow, getRhsOffset());
    Set<Integer> basicRows = new HashSet<>();
    for (int i = 0; i < coefficients.length; i++) {
        Integer basicRow = getBasicRow(getNumObjectiveFunctions() + i, true);
        if (basicRow == null) {
            coefficients[i] = 0;
        } else if (basicRows.contains(basicRow)) {
            coefficients[i] = 0;
        } else {
            basicRows.add(basicRow);
            coefficients[i] = getEntry(basicRow, getRhsOffset()) - (restrictToNonNegative ? 0 : mostNegative);
        }
    }
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}