protected void dropPhase1Objective() {
    if (getNumObjectiveFunctions() == 1) {
        return;
    }
    List<Integer> columnsToDrop = new ArrayList<Integer>();
    columnsToDrop.add(0);
    // positive cost non-artificial variables
    for (int i = getNumObjectiveFunctions(); i < getArtificialVariableOffset(); i++) {
        final double entry = tableau.getEntry(0, i);
        if (Precision.compareTo(entry, 0d, maxUlps) > 0) {
            columnsToDrop.add(i);
        }
    }
    // non-basic artificial variables
    for (int i = 0; i < getNumArtificialVariables(); i++) {
        int col = i + getArtificialVariableOffset();
        if (getBasicRow(col) == null) {
            columnsToDrop.add(col);
        }
    }
    double[][] matrix = new double[getHeight() - 1][getWidth() - columnsToDrop.size()];
    for (int i = 1; i < getHeight(); i++) {
        int col = 0;
        for (int j = 0; j < getWidth(); j++) {
            if (!columnsToDrop.contains(j)) {
                matrix[i - 1][col++] = tableau.getEntry(i, j);
            }
        }
    }
    for (int i = columnsToDrop.size() - 1; i >= 0; i--) {
        columnLabels.remove((int) columnsToDrop.get(i));
    }
    // Reset the tableau size.
    int height = getHeight() - 1;
    int width = getWidth() - columnsToDrop.size();
    this.tableau = new Array2DRowRealMatrix(height, width);
    // Copy values from old tableau to new tableau.
    for (int i = 1; i < getHeight(); i++) {
        int row = i - 1;
        int col = 0;
        for (int j = 0; j < getWidth(); j++) {
            if (!columnsToDrop.contains(j)) {
                tableau.setEntry(row, col++, tableau.getEntry(i, j));
            }
        }
    }
    // Update the number of artificial variables.
    this.numArtificialVariables = 0;
}