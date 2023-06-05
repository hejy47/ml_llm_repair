## Fixed Function 1
private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            if (row == null) {
                row = i;
            } 
            // return null only if there are multiple non-zero entries in the column
            else {
                row = null;
                break;
            }
        }
    }
    return row;
}