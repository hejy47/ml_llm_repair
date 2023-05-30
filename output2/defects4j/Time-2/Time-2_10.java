## Fixed Function 1
public int compareTo(DurationField durationField) {
    if (durationField == null) {
        throw new IllegalArgumentException("The duration field must not be null");
    }
    long thisUnitMillis = this.getUnitMillis();
    long otherUnitMillis = durationField.getUnitMillis();
    if (thisUnitMillis < otherUnitMillis) {
        return -1;
    } else if (thisUnitMillis > otherUnitMillis) {
        return 1;
    } else {
        return 0;
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
    DurationField lastUnitField = null;
    for (int i = 0; i < types.length; i++) {
        DateTimeFieldType loopType = types[i];
        DurationFieldType loopDurationType = loopType.getDurationType();
        if (loopDurationType == null) {
            throw new IllegalArgumentException("Types array must not contain a field that has a null duration type: " + loopType.getName());
        }
        DurationField loopUnitField = loopDurationType.getField(iChronology);
        if (i > 0) {
            int compare = lastUnitField.compareTo(loopUnitField);
            if (compare < 0 || (compare != 0 && loopUnitField.isSupported() == false)) {
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
        }
        lastUnitField = loopUnitField;
    }
    iTypes = (DateTimeFieldType[]) types.clone();
    chronology.validate(this, values);
    iValues = (int[]) values.clone();
}

## Fixed Function 3
//-----------------------------------------------------------------------
public Partial withField(DateTimeFieldType fieldType, int value) {
    if (fieldType == null) {
        throw new IllegalArgumentException("The field type must not be null");
    }
    int index = indexOfSupported(fieldType);
    int[] newValues = getValues();
    newValues = getField(index).set(this, index, newValues, value);
    return new Partial(this, newValues);
}

private int indexOfSupported(DateTimeFieldType fieldType) {
    int index = indexOf(fieldType);
    if (index == -1) {
        throw new IllegalArgumentException("Field '" + fieldType + "' is not supported");
    }
    return index;
}