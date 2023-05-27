## Fixed Function 1
public int getDomainAxisIndex(CategoryAxis axis) {
    if (axis != null) {
        for (int i = 0; i < this.domainAxes.size(); i++) {
            if (axis.equals(this.domainAxes.get(i))) {
                return i;
            }
        }
    }
    return -1;
}

## Fixed Function 2
public int getRangeAxisIndex(ValueAxis axis) {
    if (axis != null) {
        int index = this.rangeAxes.indexOf(axis);
        if (index >= 0) {
            return index;
        }
        
        Plot parent = getParent();
        if (parent instanceof CategoryPlot) {
            CategoryPlot p = (CategoryPlot) parent;
            return p.getRangeAxisIndex(axis);
        }
    }
    return -1;
}