private Integer getPivotRow(final int col, final SimplexTableau tableau) {
    double minRatio = Double.MAX_VALUE;
    Integer minRatioPos = null;
    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        final double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        final double entry = tableau.getEntry(i, col);
        if (Math.abs(entry) > epsilon && MathUtils.compareTo(rhs / entry, minRatio, epsilon) < 0) {
            minRatio = rhs / entry;
            minRatioPos = i;
        }
    }
    return minRatioPos;
}