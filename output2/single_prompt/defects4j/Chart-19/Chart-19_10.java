## Fixed Function 1
public int getDomainAxisIndex(CategoryAxis axis) {
    int result = this.domainAxes.indexOf(axis);
    if(result < 0) {
        // Try to find the axis by class comparison.
        for(int i = 0; i < this.domainAxes.size(); i++) {
            if(this.domainAxes.get(i).getClass().equals(axis.getClass())) {
                return i;
            }
        }
    }
    return result;
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