## Fixed Function 1
public TimeSeries createCopy(RegularTimePeriod start, RegularTimePeriod end) throws CloneNotSupportedException {
    if (start == null || end == null) {
        throw new IllegalArgumentException("Null argument.");
    }
    if (start.compareTo(end) > 0) {
        throw new IllegalArgumentException("Requires start on or before end.");
    }
    boolean emptyRange = false;
    int startIndex = getIndex(start);
    if (startIndex < 0) {
        startIndex = -(startIndex + 1);
        if (startIndex >= this.data.size()) {
            // start is after last data item
            emptyRange = true;
        }
    }
    int endIndex = getIndex(end);
    if (endIndex < 0) {
        // end period is not in original series
        endIndex = -(endIndex + 1);
        if (endIndex <= 0) {
            // end period is before first data item
            emptyRange = true;
        } else {
            // so this is last item BEFORE end
            endIndex = endIndex - 1;
        }
    }
    if (emptyRange || endIndex < startIndex) {
        TimeSeries copy = (TimeSeries) super.clone();
        copy.data = new java.util.ArrayList();
        return copy;
    } else {
        return createCopy(startIndex, endIndex);
    }
}