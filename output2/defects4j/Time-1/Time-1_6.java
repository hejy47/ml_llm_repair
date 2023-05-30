## Fixed Function 1
public int compareTo(DurationField durationField) {
    if (this == durationField) {
        return 0;
    }
    if (durationField.isSupported()) {
        return -1;
    }
    throw new IllegalArgumentException("DurationField is unsupported: " + durationField);
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
    DurationField lastUnitField = null;
    boolean seenRDT = false; // to ensure at least one RDT seen
    for (int i = 0; i < types.length; i++) {
        DateTimeFieldType loopType = types[i];
        DurationFieldType loopDurationType = loopType.getDurationType();
        if (loopDurationType == null || !loopDurationType.isSupported()) {
            throw new IllegalArgumentException("Field '" + loopType + "' is not supported");
        }
        DurationField loopUnitField = loopDurationType.getField(iChronology);
        if (i > 0) {
            int compare = lastUnitField.compareTo(loopUnitField);
            if (compare < 0) {
                throw new IllegalArgumentException("Types array must be in order largest-smallest: " + types[i - 1].getName() + " < " + loopType.getName());
            } else if (compare == 0) {
                DurationFieldType lastDurationType = types[i - 1].getDurationType();
                if (lastDurationType == null || !lastDurationType.isSupported()) {
                    throw new IllegalArgumentException("Field '" + types[i - 1] + "' is not supported");
                }
                if (seenRDT || loopDurationType == null) {
                    throw new IllegalArgumentException("Types array must not contain duplicate: " + types[i - 1].getName() + " and " + loopType.getName());
                } else {
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
        }
        seenRDT |= loopDurationType == null;
        lastUnitField = loopUnitField;
    }
    iTypes = (DateTimeFieldType[]) types.clone();
    chronology.validate(this, values);
    iValues = (int[]) values.clone();
}