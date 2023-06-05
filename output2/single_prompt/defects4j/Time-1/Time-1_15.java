## Fixed Function 1
public int compareTo(DurationField durationField) {
    if (durationField == null || !durationField.isSupported()) { // added null check
        return -1; // return -1 instead of 0
    }
    return 1;
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
        iTypes = types.clone(); // fix shallow copy
        iValues = values.clone(); // fix shallow copy
        return;
    }
    for (int i = 0; i < types.length; i++) {
        if (types[i] == null) {
            throw new IllegalArgumentException("Types array must not contain null: index " + i);
        }
    }
    DurationFieldType lastUnitType = null; // fix variable name
    for (int i = 0; i < types.length; i++) {
        DateTimeFieldType loopType = types[i];
        DurationFieldType loopUnitType = loopType.getDurationType().getFieldType();
        if (i > 0) {
            int compare = lastUnitType.compareTo(loopUnitType);
            if (compare > 0) { // fix the comparison order
                throw new IllegalArgumentException("Types array must be in order largest-smallest: " + types[i - 1].getName() + " > " + loopType.getName());
            } else if (compare == 0) {
                if (types[i - 1].getRangeDurationType() == null || loopType.getRangeDurationType() == null) { // fix null check
                    throw new IllegalArgumentException("Types array must not contain duplicate: " + types[i - 1].getName() + " and " + loopType.getName());
                } else {
                    DurationFieldType lastRangeType = types[i - 1].getRangeDurationType().getFieldType();
                    DurationFieldType loopRangeType = loopType.getRangeDurationType().getFieldType();
                    if (lastRangeType.compareTo(loopRangeType) < 0) {
                        throw new IllegalArgumentException("Types array must be in order largest-smallest: " + types[i - 1].getName() + " > " + loopType.getName());
                    }
                    if (lastRangeType.compareTo(loopRangeType) == 0) {
                        throw new IllegalArgumentException("Types array must not contain duplicate: " + types[i - 1].getName() + " and " + loopType.getName());
                    }
                }
            }
        }
        lastUnitType = loopUnitType;
    }
    iTypes = types.clone(); // fix shallow copy
    chronology.validate(this, values);
    iValues = values.clone(); // fix shallow copy
}