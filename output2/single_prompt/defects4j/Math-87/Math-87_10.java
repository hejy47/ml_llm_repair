## Fixed Function 1
private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            if (row != null) {  // fixed condition
                return null;
            } else {
                row = i;
            }
        }
    }
    return row;
}