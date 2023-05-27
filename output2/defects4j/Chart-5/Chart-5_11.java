## Fixed Function 1
public XYDataItem addOrUpdate(Number x, Number y) {
    if (x == null) {
        throw new IllegalArgumentException("Null 'x' argument.");
    }
    // if we get to here, we know that duplicate X values are not permitted
    XYDataItem overwritten = null;
    int index = indexOf(x);
    if (index >= 0 && !this.allowDuplicateXValues) {
        XYDataItem existing = (XYDataItem) this.data.get(index);
        try {
            overwritten = (XYDataItem) existing.clone();
        } catch (CloneNotSupportedException e) {
            throw new SeriesException("Couldn't clone XYDataItem!");
        }
        existing.setY(y);
    } else {
        // if the series is sorted, the negative index is a result from
        // Collections.binarySearch() and tells us where to insert the
        // new item...otherwise it will be just -1 and we should just
        // append the value to the list...
        if (this.autoSort) {
            int insertionIndex = -index - 1;
            if (insertionIndex < this.data.size()) {
                // check if the new x value is less than the next x value
                XYDataItem nextItem = this.data.get(insertionIndex);
                if (x.doubleValue() == nextItem.getXValue().doubleValue()) {
                    if (this.allowDuplicateXValues) {
                        insertionIndex++;
                    } else {
                        // throw an exception due to duplicate x values
                        throw new SeriesException("X-value already exists and duplicates are not allowed.");
                    }
                }
            }
            this.data.add(insertionIndex, new XYDataItem(x, y));
        } else {
            this.data.add(new XYDataItem(x, y));
        }
        // check if this addition will exceed the maximum item count...
        if (getItemCount() > this.maximumItemCount) {
            this.data.remove(0);
        }
    }
    fireSeriesChanged();
    return overwritten;
}