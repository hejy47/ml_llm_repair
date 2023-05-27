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
    double min = Double.POSITIVE_INFINITY;
    double max = Double.NEGATIVE_INFINITY;
    for (int r = 0; r < this.data.getRowCount(); r++) {
        for (int c = 0; c < this.data.getColumnCount(); c++) {
            BoxAndWhiskerItem item = this.data.getItem(r, c);
            if (item != null) {
                double minOutlier = Double.NaN;
                double maxOutlier = Double.NaN;
                if (item.getMinOutlier() != null) {
                    minOutlier = item.getMinOutlier().doubleValue();
                }
                if (item.getMaxOutlier() != null) {
                    maxOutlier = item.getMaxOutlier().doubleValue();
                }
                if (!Double.isNaN(minOutlier) && minOutlier < min) {
                    min = minOutlier;
                    this.minimumRangeValueRow = r;
                    this.minimumRangeValueColumn = c;
                }
                if (!Double.isNaN(maxOutlier) && maxOutlier > max) {
                    max = maxOutlier;
                    this.maximumRangeValueRow = r;
                    this.maximumRangeValueColumn = c;
                }
            }
        }
    }
    this.minimumRangeValue = min;
    this.maximumRangeValue = max;
}