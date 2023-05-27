## Fixed Function 1
public int getDomainAxisIndex(CategoryAxis axis) {
    return this.getDomainAxes().indexOf(axis);
}

## Fixed Function 2
public int getRangeAxisIndex(ValueAxis axis) {
    int result = this.getRangeAxes().indexOf(axis);
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