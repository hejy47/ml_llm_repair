## Fixed Function 1
public int getDomainAxisIndex(CategoryAxis axis) {
    int index = -1;
    for (int i = 0; i < this.domainAxes.size(); i++) {
        CategoryAxis current = (CategoryAxis) this.domainAxes.get(i);
        if (current.equals(axis)) {
            index = i;
            break;
        }
    }
    return index;
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
    return result;
}