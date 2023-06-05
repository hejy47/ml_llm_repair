## Fixed Function 1
public int getDomainAxisIndex(CategoryAxis axis) {
    int index = -1;
    for (int i = 0; i < this.domainAxes.size(); i++) {
        if (this.domainAxes.get(i).equals(axis)) {
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
        if (parent instanceof Plot) {
            result = ((Plot) parent).getRangeAxisIndex(axis);
        }
    }
    return result;
}