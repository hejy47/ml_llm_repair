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
    int col = 0; // Initialize col variable here
    for (int j = 0; j < getWidth(); j++) {
         if (!columnsToDrop.contains(j)) {
             for (int i = 1; i < getHeight(); i++) {
                 matrix[i - 1][col] = tableau.getEntry(i, j);
             }
             columnLabels.set(col, columnLabels.get(j)); // Update column labels here
             col++;
         }
    }
    columnLabels.subList(getWidth() - columnsToDrop.size(), getWidth()).clear(); // Remove unused column labels
    for (int i = columnsToDrop.size() - 1; i >= 0; i--) {
        columnLabels.remove((int) columnsToDrop.get(i));
    }
    this.tableau = new Array2DRowRealMatrix(matrix);
    this.numArtificialVariables = 0;
}