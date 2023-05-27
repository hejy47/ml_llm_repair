## Fixed Function 1
public void add(BoxAndWhiskerItem item, Comparable rowKey, Comparable columnKey) {
    this.data.addObject(item, rowKey, columnKey);
    // update cached min and max values
    int r = this.data.getRowIndex(rowKey);
    int c = this.data.getColumnIndex(columnKey);
    if ((this.maximumRangeValueRow == r && this.maximumRangeValueColumn == c) || (this.minimumRangeValueRow == r && this.minimumRangeValueColumn == c)) {
        updateBounds();
    } else {
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
}

## Fixed Function 2
private void updateBounds() {
    // iterate through all items to update min and max bounds
    double min = Double.NaN;
    double max = Double.NaN;
    for (int i = 0; i < this.data.getRowCount(); i++) {
        for (int j = 0; j < this.data.getColumnCount(); j++) {
            BoxAndWhiskerItem item = (BoxAndWhiskerItem) this.data.getObject(i, j);
            if (item != null) {
                double minval = Double.NaN;
                if (item.getMinOutlier() != null) {
                    minval = item.getMinOutlier().doubleValue();
                }
                double maxval = Double.NaN;
                if (item.getMaxOutlier() != null) {
                    maxval = item.getMaxOutlier().doubleValue();
                }
                if (Double.isNaN(min)) {
                    min = minval;
                } else if (minval < min) {
                    min = minval;
                }
                if (Double.isNaN(max)) {
                    max = maxval;
                } else if (maxval > max) {
                    max = maxval;
                }
            }
        }
    }
    this.minimumRangeValue = min;
    this.maximumRangeValue = max;
}