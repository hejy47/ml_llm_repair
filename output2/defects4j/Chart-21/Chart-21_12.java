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
    for (int i = 0; i < this.data.getRowCount(); i++) {
        for (int j = 0; j < this.data.getColumnCount(); j++) {
            List<BoxAndWhiskerItem> items = this.data.getObject(i, j);
            if (items != null) {
                for (BoxAndWhiskerItem item : items) {
                    double minval;
                    if (item.getMinOutlier() != null) {
                        minval = item.getMinOutlier().doubleValue();
                        if (minval < this.minimumRangeValue) {
                            this.minimumRangeValue = minval;
                        }
                    }
                    double maxval;
                    if (item.getMaxOutlier() != null) {
                        maxval = item.getMaxOutlier().doubleValue();
                        if (maxval > this.maximumRangeValue) {
                            this.maximumRangeValue = maxval;
                        }
                    }
                }
            }
        }
    }
    this.rangeBounds = new Range(this.minimumRangeValue, this.maximumRangeValue);
    fireDatasetChanged();
}