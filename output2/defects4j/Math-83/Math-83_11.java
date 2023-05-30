## Fixed Function 1
private Integer getBasicRow(final int col, boolean ignoreObjectiveRows) {
    Integer row = null;
    int start = ignoreObjectiveRows ? 0 : getNumObjectiveFunctions();
    for (int i = start; i < getHeight(); i++) {
        if (MathUtils.equals(getEntry(i, col), 1.0, epsilon)) {
            if (row == null) {
                row = i;
            } else {
                return null;
            }
        } else if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            return null;
        }
    }
    return row;
}

## Fixed Function 2
protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer negativeVarBasicRow = getBasicRow(getNegativeDecisionVariableOffset(), true);
    double mostNegative = negativeVarBasicRow == null ? 0 : getEntry(negativeVarBasicRow, getRhsOffset());
    Set<Integer> basicRows = new HashSet<Integer>();
    for (int i = 0; i < coefficients.length; i++) {
        Integer basicRow = getBasicRow(getNumObjectiveFunctions() + i, false);
        if (basicRow == null) {
            coefficients[i] = 0;
        } else {
            if (basicRows.contains(basicRow)) {
                // if multiple variables can take a given value
                // then we choose the first and set the rest equal to 0
                coefficients[i] = 0;
            } else {
                basicRows.add(basicRow);
                coefficients[i] = (basicRow == null ? 0 : getEntry(basicRow, getRhsOffset())) - (restrictToNonNegative ? 0 : mostNegative);
            }
        }
    }
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}