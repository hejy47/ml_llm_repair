protected RealPointValuePair getSolution() {
    double[] coefficients = new double[getOriginalNumDecisionVariables()];
    Integer basicRow = getBasicRow(getNumObjectiveFunctions() + getOriginalNumDecisionVariables());
    double mostNegative = basicRow == null ? 0 : getEntry(basicRow, getRhsOffset());
    for (int i = 0; i < coefficients.length; i++) {
        basicRow = getBasicRow(getNumObjectiveFunctions() + i);
        // if multiple variables can take a given value
        // then we choose the first and set the rest equal to 0
        coefficients[i] = (basicRow == null ? 0 : getEntry(basicRow, getRhsOffset())) - (restrictToNonNegative ? 0 : mostNegative);
        if (basicRow != null) {
            boolean foundPivotVariable = false;
            for (int j = getNumObjectiveFunctions(); j < getTablauWidth() - 1; j++) { // Fixed, used getTablauWidth() instead of getNumObjectiveFunctions() + i
                if (tableau.getEntry(basicRow, j) == 1) {
                    if(foundPivotVariable) { // Added this if statement to check if another pivot variable has already been found
                        coefficients[i] = 0;
                    } else {
                        foundPivotVariable = true;
                    }
                }
            }
        }
    }
    return new RealPointValuePair(coefficients, f.getValue(coefficients));
}