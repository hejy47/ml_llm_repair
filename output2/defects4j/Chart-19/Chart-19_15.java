## Fixed Function 1
public int getDomainAxisIndex(CategoryAxis axis) {
    for (int i = 0; i < domainAxes.size(); i++) {
        if (domainAxes.get(i).equals(axis)) {
            return i;
        }
    }
    return -1;
}

## Fixed Function 2
public int getRangeAxisIndex(ValueAxis axis) {
    int result = this.rangeAxes.indexOf(axis);
    if (result < 0) {
        // try the parent plot
        Plot parent = getParent();
        if (parent instanceof CategoryPlot) {
            CategoryPlot p = (CategoryPlot) parent;
            result = p.getRangeAxisIndex(axis);
        }
    }
    return result < 0 ? -1 : result;
}