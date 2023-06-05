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
    this.minimumRangeValue = Double.POSITIVE_INFINITY;
    this.maximumRangeValue = Double.NEGATIVE_INFINITY;
    for (int r = 0; r < this.data.getRowCount(); r++) {
        for (int c = 0; c < this.data.getColumnCount(); c++) {
            BoxAndWhiskerItem item = this.data.getItem(r, c);
            if (item != null) {
                double minval = Double.NaN;
                if (item.getMinOutlier() != null) {
                    minval = item.getMinOutlier().doubleValue();
                }
                double maxval = Double.NaN;
                if (item.getMaxOutlier() != null) {
                    maxval = item.getMaxOutlier().doubleValue();
                }
                if (!Double.isNaN(maxval)) {
                    if (maxval > this.maximumRangeValue) {
                        this.maximumRangeValue = maxval;
                        this.maximumRangeValueRow = r;
                        this.maximumRangeValueColumn = c;
                    }
                }
                if (!Double.isNaN(minval)) {
                    if (minval < this.minimumRangeValue) {
                        this.minimumRangeValue = minval;
                        this.minimumRangeValueRow = r;
                        this.minimumRangeValueColumn = c;
                    }
                }
            }
        }
    }
    this.rangeBounds = new Range(this.minimumRangeValue, this.maximumRangeValue);
    fireDatasetChanged();
}