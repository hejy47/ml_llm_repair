private Integer getPivotRow(SimplexTableau tableau, final int col) {
    // create a list of all the rows that tie for the lowest score in the minimum ratio test
    List<Integer> minRatioPositions = new ArrayList<Integer>();
    double minRatio = Double.MAX_VALUE;
    for (int i = tableau.getNumObjectiveFunctions(); i < tableau.getHeight(); i++) {
        double rhs = tableau.getEntry(i, tableau.getWidth() - 1);
        double entry = tableau.getEntry(i, col);
        if (Precision.compareTo(entry, 0d, maxUlps) > 0) {
            double ratio = rhs / entry;
            // check if the entry is strictly equal to the current min ratio
            // do not use a ulp/epsilon check
            int cmp = Double.compare(ratio, minRatio);
            if (cmp == 0) {
                minRatioPositions.add(i);
            } else if (cmp < 0) {
                minRatio = ratio;
                minRatioPositions.clear();
                minRatioPositions.add(i);
            }
        }
    }
    if (minRatioPositions.isEmpty()) {
        return null;
    } else if (minRatioPositions.size() > 1) {
        // there's a degeneracy as indicated by a tie in the minimum ratio test
        // 1. check if there's an artificial variable that can be forced out of the basis
        for (Integer row : minRatioPositions) {
            int offset = tableau.getArtificialVariableOffset();
            int numArtificial = tableau.getNumArtificialVariables();
            for (int i = 0; i < numArtificial; i++) {
                int column = i + offset;
                double entry = tableau.getEntry(row, column);
                if (Precision.equals(entry, 1d, maxUlps) && row.equals(tableau.getBasicRow(column))) {
                    return row;
                }
            }
        }
        // 2. apply Bland's rule to prevent cycling:
        //    take the row for which the corresponding basic variable has the smallest index
        //
        // see http://www.stanford.edu/class/msande310/blandrule.pdf
        // see http://en.wikipedia.org/wiki/Bland%27s_rule (not equivalent to the above paper)
        //
        // Additional heuristic: if we did not get a solution after half of maxIterations
        //                       revert to the simple case of just returning the top-most row
        // This heuristic is based on empirical data gathered while investigating MATH-828.
        Integer minRow = null;
        int minIndex = tableau.getWidth();
        for (Integer row : minRatioPositions) {
            int start = tableau.getNumObjectiveFunctions();
            int end = tableau.getWidth() - 1;
            if (minRow != null) {
                int cmp = Double.compare(tableau.getEntry(row, start + minIndex),
                        tableau.getEntry(minRow, start + minIndex));
                if (cmp < 0) {
                    minIndex = tableau.getBasicColumn(row);
                    minRow = row;
                } else if (cmp > 0) {
                    continue;
                }
            } else {
                for (int i = start; i < end; i++) {
                    if (tableau.getEntry(row, i) != 0) {
                        minIndex = i - start;
                        minRow = row;
                        break;
                    }
                }
            }
        }
        return minRow;
    }
    return minRatioPositions.get(0);
}