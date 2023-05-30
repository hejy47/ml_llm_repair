## Fixed Function 1
public int compareTo(DurationField durationField) {
    if (this == durationField) {
        return 0;
    } else if (durationField.isSupported()) {
        return -1;
    } else {
        throw new IllegalArgumentException("The field " + durationField.getName()
                + " is not supported by this duration");
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
    for (int i = 0; i < types.length; i++) {
        if (types[i] == null) {
            throw new IllegalArgumentException("Types array must not contain null: index " + i);
        }
    }
    DurationFieldType lastType = null;
    for (int i = 0; i < types.length; i++) {
        DateTimeFieldType loopType = types[i];
        DurationFieldType loopFieldType = loopType.getDurationType();
        if (i > 0) {
            if (lastType.isPrecise() && loopFieldType.isPrecise()) {
                if (lastType.compareTo(loopFieldType) < 0) {
                    throw new IllegalArgumentException("Types array must be in order largest-smallest: " + types[i - 1].getName() + " < " + loopType.getName());
                } else if (lastType == loopFieldType) {
                    throw new IllegalArgumentException("Types array must not contain duplicate: " + types[i - 1].getName() + " and " + loopType.getName());
                }
            } else {
                int cmp = lastType.compareTo(loopFieldType);
                if (cmp < 0) {
                    if (lastType.getRangeDurationType().compareTo(loopFieldType) < 0) {
                        throw new IllegalArgumentException("Types array must be in order largest-smallest: " + types[i - 1].getName() + " < " + loopType.getName());
                    }
                } else if (cmp == 0) {
                    if (types[i - 1].getRangeDurationType() == null) {
                        if (loopType.getRangeDurationType() == null) {
                            throw new IllegalArgumentException("Types array must not contain duplicate: " + types[i - 1].getName() + " and " + loopType.getName());
                        }
                    } else {
                        if (loopType.getRangeDurationType() == null) {
                            throw new IllegalArgumentException("Types array must be in order largest-smallest: " + types[i - 1].getName() + " < " + loopType.getName());
                        }
                        DurationFieldType lastRangeField = types[i - 1].getRangeDurationType();
                        DurationFieldType loopRangeField = loopType.getRangeDurationType();
                        if (lastRangeField.compareTo(loopRangeField) < 0) {
                            throw new IllegalArgumentException("Types array must be in order largest-smallest: " + types[i - 1].getName() + " < " + loopType.getName());
                        }
                        if (lastRangeField == loopRangeField) {
                            throw new IllegalArgumentException("Types array must not contain duplicate: " + types[i - 1].getName() + " and " + loopType.getName());
                        }
                    }
                } else {
                    if (lastFieldType.compareTo(loopType.getRangeDurationType()) > 0) {
                        throw new IllegalArgumentException("Types array must be in order largest-smallest: " + types[i - 1].getName() + " < " + loopType.getName());
                    }
                }
            }
        }
        lastType = loopFieldType;
    }
    iTypes = (DateTimeFieldType[]) types.clone();
    chronology.validate(this, values);
    iValues = (int[]) values.clone();
}