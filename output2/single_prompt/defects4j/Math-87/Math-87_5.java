## Fixed Function 1
private Integer getBasicRow(final int col) {
    Integer row = null;
    for (int i = getNumObjectiveFunctions(); i < getHeight(); i++) {
        if (!MathUtils.equals(getEntry(i, col), 0.0, getEpsilon())) {
            if (row == null) {
                row = i;
            } else {
                return null;
            }
        }
    }
    return row;
} 

(The fix involves replacing "epsilon" with a method call to "getEpsilon()" which properly retrieves the value of the epsilon parameter.)