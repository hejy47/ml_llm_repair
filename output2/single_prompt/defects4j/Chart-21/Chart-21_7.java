## Fixed Function 1
public void add(BoxAndWhiskerItem item, Comparable rowKey, Comparable columnKey) {
    this.data.addObject(item, rowKey, columnKey);
    // update cached min and max values
    int r = this.data.getRowIndex(rowKey);
    int c = this.data.getColumnIndex(columnKey);
    if ((this.maximumRangeValueRow == r && this.maximumRangeValueColumn == c) || (this.minimumRangeValueRow == r && this.minimumRangeValueColumn == c)) {
        updateBounds();
    }
    double minval = Double.NaN;
    if (item.getMinOutlier() != null) {
        minval = item.getMinOutlier().doubleValue();
    }
    double maxval = Double.NaN;
    if (item.getMaxOutlier() != null) {
        maxval = item.getMaxOutlier().doubleValue();
    }
    if (Double.isNaN(this.maximumRangeValue)) {
        this.maximumRangeValue = maxval;
        this.maximumRangeValueRow = r;
        this.maximumRangeValueColumn = c;
    } else if (maxval > this.maximumRangeValue) {
        this.maximumRangeValue = maxval;
        this.maximumRangeValueRow = r;
        this.maximumRangeValueColumn = c;
    }
    if (Double.isNaN(this.minimumRangeValue)) {
        this.minimumRangeValue = minval;
        this.minimumRangeValueRow = r;
        this.minimumRangeValueColumn = c;
    } else if (minval < this.minimumRangeValue) {
        this.minimumRangeValue = minval;
        this.minimumRangeValueRow = r;
        this.minimumRangeValueColumn = c;
    }
    // Check if new range bounds should be set
    if (this.minimumRangeValue < this.rangeBounds.getLowerBound() || this.maximumRangeValue > this.rangeBounds.getUpperBound()) {
        this.rangeBounds = new Range(this.minimumRangeValue, this.maximumRangeValue);
    }
    fireDatasetChanged();
}

## Fixed Function 2
private void updateBounds() {
    this.minimumRangeValue = Double.POSITIVE_INFINITY;
    this.maximumRangeValue = Double.NEGATIVE_INFINITY;
}