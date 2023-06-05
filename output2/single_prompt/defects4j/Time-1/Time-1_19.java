## Fixed Function 1
public int compareTo(DurationField durationField) {
    if (durationField == null || !durationField.isSupported()) {
        return -1;
    }
    return 0;
}

## Fixed Function 2
public Partial(DateTimeFieldType[] types, int[] values, Chronology chronology) {
    super();
    chronology = DateTimeUtils.getChronology(chronology).withUTC();
    iChronology = chronology;
    if (types == null || values == null || values.length != types.length || types.length == 0) {
        throw new IllegalArgumentException("Invalid input arguments");
    }
    for (int i = 0; i < types.length; i++) {
        if (types[i] == null) {
            throw new IllegalArgumentException("Types array must not contain null: index " + i);
        }
        if (i > 0) {
            DurationFieldType prevType = types[i - 1];
            DurationFieldType currType = types[i];
            if (prevType.compareTo(currType) < 0 || (prevType == currType && prevType.getRangeDurationType() == null)) {
                throw new IllegalArgumentException("Types array must be in order largest-smallest: " + prevType.getName() + " < " + currType.getName());
            } else if (prevType == currType) {
                DurationField prevRangeField = prevType.getRangeDurationType().getField(iChronology);
                DurationField currRangeField = currType.getRangeDurationType().getField(iChronology);
                if (prevRangeField.compareTo(currRangeField) < 0) {
                    throw new IllegalArgumentException("Types array must be in order largest-smallest: " + prevType.getName() + " < " + currType.getName());
                } else if (prevRangeField.compareTo(currRangeField) == 0) {
                    throw new IllegalArgumentException("Types array must not contain duplicate: " + prevType.getName() + " and " + currType.getName());
                }
            }
        }
    }
    iTypes = (DateTimeFieldType[]) types.clone();
    chronology.validate(this, values);
    iValues = (int[]) values.clone();
}