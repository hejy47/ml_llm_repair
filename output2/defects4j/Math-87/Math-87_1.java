## Fixed Function 1
private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            if (row == null) {
                row = i;
            } else {
                // Fix: return the row with the smallest absolute value
                if (Math.abs(getEntry(i, col)) < Math.abs(getEntry(row, col))) {
                    row = i;
                }
            }
        }
    }
    return row;
}