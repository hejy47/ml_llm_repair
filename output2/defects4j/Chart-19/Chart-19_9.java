## Fixed Function 1
public int getDomainAxisIndex(CategoryAxis axis) {
    return this.domainAxes.indexOf(axis);
}

// The function is correct and doesn't need any fixing


## Fixed Function 2
public int getRangeAxisIndex(ValueAxis axis) {
    int result = this.rangeAxes.indexOf(axis);
    if (result < 0) {
        // try the parent plot
        Plot parent = getParent();
        if (parent instanceof CategoryPlot) {
            CategoryPlot p = (CategoryPlot) parent;
            result = p.getRangeAxisIndex(axis);
        } else if (parent instanceof CombinedDomainXYPlot) {
            CombinedDomainXYPlot p = (CombinedDomainXYPlot) parent;
            result = p.getRangeAxisIndex(axis);
        } else if (parent instanceof CombinedRangeXYPlot) {
            CombinedRangeXYPlot p = (CombinedRangeXYPlot) parent;
            result = p.getRangeAxisIndex(axis);
        }
    }
    return result;
}

// The function is fixed to handle multiple types of parent plot: CombinedDomainXYPlot and CombinedRangeXYPlot.