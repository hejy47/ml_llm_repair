## Fixed Function 1
public TimeSeries createCopy(RegularTimePeriod start, RegularTimePeriod end) throws CloneNotSupportedException {
    if (start == null) {
        throw new IllegalArgumentException("Null 'start' argument.");
    }
    if (end == null) {
        throw new IllegalArgumentException("Null 'end' argument.");
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
        if (-endIndex - 1 >= this.data.size()) {
            // end is after last data item
            emptyRange = true;
        } else {
            endIndex = -endIndex - 2;  // end is before last data item
        }
    }
    if (endIndex < startIndex) {
        emptyRange = true;
    }
    if (emptyRange) {
        TimeSeries copy = (TimeSeries) super.clone();
        copy.data = new ArrayList<>();
        return copy;
    } else {
        return createCopy(startIndex, endIndex);
    }
}