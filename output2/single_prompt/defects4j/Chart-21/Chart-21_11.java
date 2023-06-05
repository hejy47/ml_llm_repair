## Fixed Function 1
public void add(BoxAndWhiskerItem item, Comparable rowKey, Comparable columnKey) {
    this.data.addObject(item, rowKey, columnKey);
    // update cached min and max values
    int r = this.data.getRowIndex(rowKey);
    int c = this.data.getColumnIndex(columnKey);
    if ((this.maximumRangeValueRow == r && this.maximumRangeValueColumn == c) || (this.minimumRangeValueRow == r && this.minimumRangeValueColumn == c)) {
        updateMinMaxValues();
    }
    this.rangeBounds = new Range(this.minimumRangeValue, this.maximumRangeValue);
    fireDatasetChanged();
}

private void updateMinMaxValues() {
    int rowCount = this.data.getRowCount();
    int columnCount = this.data.getColumnCount();
    this.minimumRangeValue = Double.NaN;
    this.maximumRangeValue = Double.NaN;
    for (int r = 0; r < rowCount; r++) {
        for (int c = 0; c < columnCount; c++) {
            BoxAndWhiskerItem item = (BoxAndWhiskerItem) this.data.getObject(r, c);
            if (item != null) {
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
            }
        }
    }
}

## Fixed Function 2
private void updateBounds() {
    updateMinMaxValues();
}