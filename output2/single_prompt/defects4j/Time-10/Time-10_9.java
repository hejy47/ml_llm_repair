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
        if (start.getFieldType(i) != end.getFieldType(i)) {
            throw new IllegalArgumentException("ReadablePartial objects must have the same set of fields");
        }
        if (start.getValue(i) < start.getField(i).getMinimumValue() || start.getValue(i) > start.getField(i).getMaximumValue()) {
            throw new IllegalArgumentException("Value of the field exceeds boundary limits");
        }
        if (end.getValue(i) < end.getField(i).getMinimumValue() || end.getValue(i) > end.getField(i).getMaximumValue()) {
            throw new IllegalArgumentException("Value of the field exceeds boundary limits");
        }
    }
    if (DateTimeUtils.isContiguous(start) == false) {
        throw new IllegalArgumentException("ReadablePartial objects must be contiguous");
    }
    Chronology chrono = DateTimeUtils.getChronology(start.getChronology()).withUTC();
    int[] values = chrono.get(zeroInstance, chrono.set(start, 0L), chrono.set(end, 0L));
    return values[0];
}