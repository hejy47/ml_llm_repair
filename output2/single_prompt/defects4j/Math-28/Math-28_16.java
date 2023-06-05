private Integer getPivotRow(SimplexTableau tableau, final int col) {
    List<Integer> minRatioPositions = new ArrayList<Integer>();
    double minRatio = Double.MAX_VALUE;
    int numObjectives = tableau.getNumObjectiveFunctions();
    int height = tableau.getHeight();
    for (int i = numObjectives; i < height; i++) {
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);
        if (entry <= 0) {
            continue;
        }
        final double ratio = rhs / entry;
        if (ratio < minRatio) {
            minRatio = ratio;
            minRatioPositions.clear();
            minRatioPositions.add(i);
        } else if (ratio == minRatio) {
            minRatioPositions.add(i);
        }
    }
    if (minRatioPositions.size() == 0) {
        return null;
    } else if (minRatioPositions.size() > 1) {
        Integer minRow = null;
        int minIndex = tableau.getWidth();
        for (Integer row : minRatioPositions) {
            int i = numObjectives;
            for (; i < tableau.getWidth() - 1 && minRow != row; i++) {
                if (row == tableau.getBasicRow(i)) {
                    if (i < minIndex) {
                        minIndex = i;
                        minRow = row;
                    }
                }
            }
        }
        return minRow;
    }
    return minRatioPositions.get(0);
}