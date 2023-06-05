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
    double minVal = Double.POSITIVE_INFINITY;
    double maxVal = Double.NEGATIVE_INFINITY;
    for (int r = 0; r < this.data.getRowCount(); r++) {
        for (int c = 0; c < this.data.getColumnCount(); c++) {
            BoxAndWhiskerItem item = (BoxAndWhiskerItem) this.data.getObject(r, c);
            if (item != null) {
                double minValue = item.getMinOutlier() != null ? item.getMinOutlier().doubleValue() : Double.NaN;
                if (!Double.isNaN(minValue) && minValue < minVal) {
                    minVal = minValue;
                    this.minimumRangeValueRow = r;
                    this.minimumRangeValueColumn = c;
                }
                double maxValue = item.getMaxOutlier() != null ? item.getMaxOutlier().doubleValue() : Double.NaN;
                if (!Double.isNaN(maxValue) && maxValue > maxVal) {
                    maxVal = maxValue;
                    this.maximumRangeValueRow = r;
                    this.maximumRangeValueColumn = c;
                }
            }
        }
    }
    this.minimumRangeValue = minVal == Double.POSITIVE_INFINITY ? Double.NaN : minVal;
    this.maximumRangeValue = maxVal == Double.NEGATIVE_INFINITY ? Double.NaN : maxVal;
}