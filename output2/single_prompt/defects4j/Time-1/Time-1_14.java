## Fixed Function 1
public int compareTo(DurationField durationField) {
    if (this == durationField) {
        return 0;
    }
    if (durationField == null || !durationField.isSupported()) {
        throw new IllegalArgumentException("Invalid duration field passed in");
    }
    return -1;
}

## Fixed Function 2
public Partial(DateTimeFieldType[] types, int[] values, Chronology chronology) {
    super();
    chronology.getZone();
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
        iTypes = types.clone();
        iValues = values.clone();
        return;
    }
    for (int i = 0; i < types.length; i++) {
        if (types[i] == null) {
            throw new IllegalArgumentException("Types array must not contain null: index " + i);
        }
    }
    DurationField lastUnitField = null;
    for (int i = 0; i < types.length; i++) {
        DateTimeFieldType loopType = types[i];
        DurationFieldType loopDurationType = loopType.getDurationType();
        if (i > 0) {
            DurationField loopUnitField = loopDurationType.getField(iChronology);
            int compare = lastUnitField.compareTo(loopUnitField);
            if (compare < 0) {
                throw new IllegalArgumentException("Types array must be in order largest-smallest: " + types[i - 1].getName() + " < " + loopType.getName());
            } else if (compare == 0) {
                if (types[i - 1].getRangeDurationType() == null) {
                    if (loopType.getRangeDurationType() == null) {
                        throw new IllegalArgumentException("Types array must not contain duplicate: " + types[i - 1].getName() + " and " + loopType.getName());
                    }
                } else {
                    if (loopType.getRangeDurationType() == null) {
                        throw new IllegalArgumentException("Types array must be in order largest-smallest: " + types[i - 1].getName() + " < " + loopType.getName());
                    }
                    DurationField lastRangeField = types[i - 1].getRangeDurationType().getField(iChronology);
                    DurationField loopRangeField = loopType.getRangeDurationType().getField(iChronology);
                    if (lastRangeField.compareTo(loopRangeField) < 0) {
                        throw new IllegalArgumentException("Types array must be in order largest-smallest: " + types[i - 1].getName() + " < " + loopType.getName());
                    }
                    if (lastRangeField.compareTo(loopRangeField) == 0) {
                        throw new IllegalArgumentException("Types array must not contain duplicate: " + types[i - 1].getName() + " and " + loopType.getName());
                    }
                }
            }
            lastUnitField = loopUnitField;
        } else {
            lastUnitField = loopDurationType.getField(iChronology);
        }
    }
    iTypes = types.clone();
    chronology.validate(this, values);
    iValues = values.clone();
}