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
    int newNumColumns = getWidth() - columnsToDrop.size();
    double[][] matrix = new double[getHeight() - 1][newNumColumns];
    int col = 0;
    for (int j = 0; j < getWidth(); j++) {
        if (!columnsToDrop.contains(j)) {
            for (int i = 1; i < getHeight(); i++) {
                matrix[i - 1][col] = tableau.getEntry(i, j);
            }
            columnLabels.set(col, columnLabels.get(j));
            col++;
        }
    }
    this.tableau = new Array2DRowRealMatrix(matrix);
    this.numArtificialVariables = 0;
    this.numSlackVariables = newNumColumns - getNumObjectiveFunctions();
    this.artificialVariableOffset = newNumColumns - getNumArtificialVariables();
}