## Fixed Function 1
//-----------------------------------------------------------------------
protected static int between(ReadablePartial start, ReadablePartial end, ReadablePeriod zeroInstance) {
    if (start == null || end == null) {
        throw new IllegalArgumentException("ReadablePartial objects must not be null");
    }
    if (start.size() != end.size()) {
        throw new IllegalArgumentException("ReadablePartial objects must have the same set of fields");
    }
    for (int i = 0, isize = start.size(); i < isize; i++) {
        if (start.getField(i).getType() != end.getField(i).getType()) {
            throw new IllegalArgumentException("ReadablePartial objects must have the same set of fields");
        }
    }
    if (DateTimeUtils.isContiguous(start) == false) {
        throw new IllegalArgumentException("ReadablePartial objects must be contiguous");
    }
    Chronology chrono = DateTimeUtils.getChronology(start.getChronology()).withUTC();
    int[] values = chrono.get(zeroInstance, start, end);
    return values[0];
}