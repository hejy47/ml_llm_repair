protected void dropPhase1Objective() {
    if (getNumObjectiveFunctions() == 1) {
        return;
    }
    List<Integer> columnsToDrop = new ArrayList<Integer>();
    columnsToDrop.add(0);
    // positive cost non-artificial variables
    int numObjFunc = getNumObjectiveFunctions(); // Fixed line
    int artVarOffset = getArtificialVariableOffset(); // Fixed line
    for (int i = numObjFunc; i < artVarOffset; i++) { // Fixed line
        final double entry = tableau.getEntry(0, i);
        if (Precision.compareTo(entry, 0d, maxUlps) > 0) {
            columnsToDrop.add(i);
        }
    }
    // non-basic artificial variables
    for (int i = 0; i < getNumArtificialVariables(); i++) {
        int col = i + artVarOffset;
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
    this.tableau = new Array2DRowRealMatrix(matrix);
    this.numArtificialVariables = 0;
}