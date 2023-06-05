## Fixed Function 1
public int compareTo(DurationField durationField) {
    if (durationField == null || !durationField.isSupported()) {
        return -1; // return -1 as this field is smaller than the given durationField
    } else {
        return 0; // return 0 as both fields are of the same type
    }
}

## Fixed Function 2
public Partial(DateTimeFieldType[] types, int[] values, Chronology chronology) {
    super();
    chronology = DateTimeUtils.getChronology(chronology).withUTC();
    iChronology = chronology;
    if (types == null) {
        throw new IllegalArgumentException("Types array must not be null");
    }
    if (values == null) {
        throw new IllegalArgumentException("Values array must not be null");
    }
    if (values.length != types.length) {
        throw new IllegalArgumentException("Values array must be the same length as the types array");
    }
    if (types.length == 0) {
        iTypes = types;
        iValues = values;
        return;
    }
    DateTimeFieldType lastType = null;
    DurationField lastUnitField = null;
    for (int i = 0; i < types.length; i++) {
        if (types[i] == null) {
            throw new IllegalArgumentException("Types array must not contain null: index " + i);
        }
        DateTimeFieldType loopType = types[i];
        DurationField loopUnitField = loopType.getDurationType().getField(iChronology);
        if (lastType != null) {
            int compare = lastUnitField.compareTo(loopUnitField);
            if (compare > 0 || (compare == 0 && lastType.compareTo(loopType) > 0)) {
                throw new IllegalArgumentException("Types array must be in order largest-smallest: " + lastType.getName() + " < " + loopType.getName());
            }
        }
        lastType = loopType;
        lastUnitField = loopUnitField;
    }
    iTypes = (DateTimeFieldType[]) types.clone();
    chronology.validate(this, values);
    iValues = (int[]) values.clone();
}