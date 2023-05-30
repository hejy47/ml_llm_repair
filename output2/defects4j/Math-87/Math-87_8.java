## Fixed Function 1
private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        if (!MathUtils.equals(getEntry(i, col), 0.0, epsilon)) {
            if (row == null) {
                //store the row as the potentially basic row and continue
                row = i;
            } else {
                //there are multiple potentially basic rows, so return null
                return null;
            }
        }
    }
    return row;
}