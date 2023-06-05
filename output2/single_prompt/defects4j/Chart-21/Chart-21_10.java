## Fixed Function 1
public void add(BoxAndWhiskerItem item, Comparable rowKey, Comparable columnKey) {
    this.data.addObject(item, rowKey, columnKey);
    // update cached min and max values
    int r = this.data.getRowIndex(rowKey);
    int c = this.data.getColumnIndex(columnKey);
    if ((this.maximumRangeValueRow == r && this.maximumRangeValueColumn == c) || (this.minimumRangeValueRow == r && this.minimumRangeValueColumn == c)) {
        updateBounds(r, c);
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
private void updateBounds(int r, int c) {
    if (r == this.maximumRangeValueRow && c == this.maximumRangeValueColumn) {
        this.maximumRangeValue = calculateMaximumRangeValue();
    } else if (r == this.minimumRangeValueRow && c == this.minimumRangeValueColumn) {
        this.minimumRangeValue = calculateMinimumRangeValue();
    }
}

private double calculateMaximumRangeValue() {
    double max = Double.NaN;
    int rowCount = this.data.getRowCount();
    int columnCount = this.data.getColumnCount();
    for (int r = 0; r < rowCount; r++) {
        for (int c = 0; c < columnCount; c++) {
            BoxAndWhiskerItem item = this.data.getItem(r, c);
            if (item != null && item.getMaxOutlier() != null) {
                double outlier = item.getMaxOutlier().doubleValue();
                if (Double.isNaN(max)) {
                    max = outlier;
                } else if (outlier > max) {
                    max = outlier;
                }
            }
        }
    }
    return max;
}

private double calculateMinimumRangeValue() {
    double min = Double.NaN;
    int rowCount = this.data.getRowCount();
    int columnCount = this.data.getColumnCount();
    for (int r = 0; r < rowCount; r++) {
        for (int c = 0; c < columnCount; c++) {
            BoxAndWhiskerItem item = this.data.getItem(r, c);
            if (item != null && item.getMinOutlier() != null) {
                double outlier = item.getMinOutlier().doubleValue();
                if (Double.isNaN(min)) {
                    min = outlier;
                } else if (outlier < min) {
                    min = outlier;
                }
            }
        }
    }
    return min;
}