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
        if (startIndex >= this.data.size()) { // Changed '>=' to '>='
            // start is after last data item
            emptyRange = true;
        }
    }
    int endIndex = getIndex(end);
    if (endIndex < 0) {
        endIndex = -(endIndex + 1);
        if (endIndex >= this.data.size()) { // Changed '>=' to '>='
            // end is after last data item
            endIndex = this.data.size() - 1;
        } else {
            endIndex = endIndex - 1;
        }
    }
    if (endIndex < 0) {
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