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
        if (startIndex >= this.data.size()) { // Fixed comparison to include equal case
            // start is after last data item
            emptyRange = true;
        }
    }
    int endIndex = getIndex(end);
    if (endIndex < 0) {
        endIndex = -(endIndex + 1);
        if (endIndex <= 0) { // Fixed if statement to include less than or equal to case
            // end is before first data item
            emptyRange = true;
        } else {
          endIndex = endIndex - 1;
        }
    }
    if (endIndex < startIndex) { // Fixed condition to check if endIndex comes before startIndex
        emptyRange = true;
    }
    if (emptyRange) {
        TimeSeries copy = (TimeSeries) super.clone();
        copy.data = new java.util.ArrayList();
        return copy;
    } else {
        return createCopy(startIndex, endIndex);
    }
}