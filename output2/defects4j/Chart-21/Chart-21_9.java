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
    this.rangeBounds = new Range(this.minimumRangeValue, this.maximumRangeValue);
    fireDatasetChanged();
}

## Fixed Function 2
private void updateBounds() {
    double minVal = Double.POSITIVE_INFINITY;
    double maxVal = Double.NEGATIVE_INFINITY;
    for (int row = 0; row < data.getRowCount(); row++) {
        for (int column = 0; column < data.getColumnCount(); column++) {
            BoxAndWhiskerItem item = data.getItem(row, column);
            if (item != null) {
                if (item.getMinOutlier() != null) {
                    double minOutlierVal = item.getMinOutlier().doubleValue();
                    if (minOutlierVal < minVal) {
                        minVal = minOutlierVal;
                        minimumRangeValueRow = row;
                        minimumRangeValueColumn = column;
                    }
                }
                if (item.getMaxOutlier() != null) {
                    double maxOutlierVal = item.getMaxOutlier().doubleValue();
                    if (maxOutlierVal > maxVal) {
                        maxVal = maxOutlierVal;
                        maximumRangeValueRow = row;
                        maximumRangeValueColumn = column;
                    }
                }
            }
        }
    }
    minimumRangeValue = minVal == Double.POSITIVE_INFINITY ? Double.NaN : minVal;
    maximumRangeValue = maxVal == Double.NEGATIVE_INFINITY ? Double.NaN : maxVal;
}