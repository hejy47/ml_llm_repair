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
    for (int i = 0; i < types.length; i++) {
        if (types[i] == null) {
            throw new IllegalArgumentException("Types array must not contain null: index " + i);
        }
    }
    DurationFieldType lastFieldType = null;
    DurationField lastUnitField = null;
    for (int i = 0; i < types.length; i++) {
        DateTimeFieldType loopType = types[i];
        DurationFieldType loopFieldType = loopType.getDurationType();
        if (lastFieldType != null) {
            if (lastFieldType.isPrecise() && !loopFieldType.isPrecise()) {
                throw new IllegalArgumentException("Types array must be precise until largest imprecise type: " + lastFieldType);
            }
            if (lastUnitField.compareTo(loopType.getDurationField()) < 0) {
                throw new IllegalArgumentException("Types array must be in order largest-smallest: " + lastFieldType.getName() + " < " + loopFieldType.getName());
            }
            if (lastUnitField.compareTo(loopType.getDurationField()) == 0) {
                if (lastFieldType.equals(loopFieldType)) {
                    throw new IllegalArgumentException("Types array must not contain duplicate: " + lastFieldType.getName() + " and " + loopFieldType.getName());
                }
                if (lastFieldType.getRangeDurationType() == null) {
                    throw new IllegalArgumentException("Types array must not contain duplicate: " + lastFieldType.getName() + " and " + loopFieldType.getName());
                }
                DurationField lastRangeField = lastFieldType.getRangeDurationType().getField(iChronology);
                DurationField loopRangeField = loopFieldType.getRangeDurationType().getField(iChronology);
                if (lastRangeField.compareTo(loopRangeField) < 0) {
                    throw new IllegalArgumentException("Types array must be in order largest-smallest: " + lastFieldType.getName() + " < " + loopFieldType.getName());
                }
                if (lastRangeField.compareTo(loopRangeField) == 0) {
                    throw new IllegalArgumentException("Types array must not contain duplicate: " + lastFieldType.getName() + " and " + loopFieldType.getName());
                }
            }
        }
        lastFieldType = loopFieldType;
        lastUnitField = loopType.getDurationField();
    }
    iTypes = (DateTimeFieldType[]) types.clone();
    chronology.validate(this, values);
    iValues = (int[]) values.clone();
}