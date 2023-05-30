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
    DurationFieldType lastUnitType = null;
    for (int i = 0; i < types.length; i++) {
        DateTimeFieldType fieldType = types[i];
        DurationFieldType unitType = fieldType.getDurationType();
        if (unitType == null) {
            throw new IllegalArgumentException("Types array must not contain a field that has a null duration type: " + fieldType.getName());
        }
        if (i > 0) {
            int compare = unitType.compareTo(lastUnitType);
            if (compare > 0) {
                throw new IllegalArgumentException("Types array must be in order largest-smallest: " + lastUnitType.getName() + " < " + unitType.getName());
            } else if (compare == 0) {
                if (types[i - 1].getRangeDurationType() == null || fieldType.getRangeDurationType() == null) {
                    throw new IllegalArgumentException("Types array must not contain duplicate: " + types[i - 1].getName() + " and " + fieldType.getName());
                } else {
                    DurationFieldType lastRangeType = types[i - 1].getRangeDurationType();
                    DurationFieldType rangeType = fieldType.getRangeDurationType();
                    int rangeCompare = rangeType.compareTo(lastRangeType);
                    if (rangeCompare < 0) {
                        throw new IllegalArgumentException("Types array must be in order largest-smallest: " + lastRangeType.getName() + " < " + rangeType.getName());
                    } else if (rangeCompare == 0) {
                        throw new IllegalArgumentException("Types array must not contain duplicate: " + types[i - 1].getName() + " and " + fieldType.getName());
                    }
                }
            }
        }
        lastUnitType = unitType;
    }
    iTypes = (DateTimeFieldType[]) types.clone();
    chronology.validate(this, values);
    iValues = (int[]) values.clone();
}