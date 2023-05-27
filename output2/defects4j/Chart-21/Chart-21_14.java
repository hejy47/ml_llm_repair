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
    if (Double.isNaN(this.maximumRangeValue) || maxval > this.maximumRangeValue) {
        this.maximumRangeValue = maxval;
        this.maximumRangeValueRow = r;
        this.maximumRangeValueColumn = c;
    }
    if (Double.isNaN(this.minimumRangeValue) || minval < this.minimumRangeValue) {
        this.minimumRangeValue = minval;
        this.minimumRangeValueRow = r;
        this.minimumRangeValueColumn = c;
    }
    this.rangeBounds = new Range(this.minimumRangeValue, this.maximumRangeValue);
    fireDatasetChanged();
}

## Fixed Function 2
private void updateBounds() {
    this.minimumRangeValue = Double.POSITIVE_INFINITY;
    this.maximumRangeValue = Double.NEGATIVE_INFINITY;
    for (int r = 0; r < data.getRowCount(); r++) {
        for (int c = 0; c < data.getColumnCount(); c++) {
            BoxAndWhiskerItem item = (BoxAndWhiskerItem) data.getObject(r, c);
            if (item != null) {
                Number min = item.getMinOutlier();
                Number max = item.getMaxOutlier();
                if (min != null && min.doubleValue() < this.minimumRangeValue) {
                    this.minimumRangeValue = min.doubleValue();
                    this.minimumRangeValueRow = r;
                    this.minimumRangeValueColumn = c;
                }
                if (max != null && max.doubleValue() > this.maximumRangeValue) {
                    this.maximumRangeValue = max.doubleValue();
                    this.maximumRangeValueRow = r;
                    this.maximumRangeValueColumn = c;
                }
            }
        }
    }
}