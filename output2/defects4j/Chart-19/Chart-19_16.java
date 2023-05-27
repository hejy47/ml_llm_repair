## Fixed Function 1
public int getDomainAxisIndex(CategoryAxis axis) {
    for(int i=0; i<this.domainAxes.size(); i++){
        if(this.domainAxes.get(i) == axis){
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
    return result;
}