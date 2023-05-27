## Fixed Function 1
public int getDomainAxisIndex(CategoryAxis axis) {
    if (this.domainAxes != null) {
        return this.domainAxes.indexOf(axis);
    } else {
        return -1;
    }
}

## Fixed Function 2
public int getRangeAxisIndex(ValueAxis axis) {
    int result = -1;
    if (this.rangeAxes != null) {
        result = this.rangeAxes.indexOf(axis);
    }
    if (result < 0) {
        // try the parent plot
        Plot parent = getParent();
        if (parent instanceof CategoryPlot) {
            CategoryPlot p = (CategoryPlot) parent;
            result = p.getRangeAxisIndex(axis);
        }
    }
    return result;
}