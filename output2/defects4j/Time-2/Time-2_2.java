## Fixed Function 1
public int compareTo(DurationField durationField) {
    if (durationField instanceof PreciseDurationField) {
        PreciseDurationField other = (PreciseDurationField) durationField;
        long otherUnitMillis = other.getUnitMillis();
        long thisUnitMillis = getUnitMillis();
        return (thisUnitMillis < otherUnitMillis ? -1 : (thisUnitMillis == otherUnitMillis ? 0 : 1));
    }
    long thisDuration = getUnitMillis();
    long otherDuration = durationField.getUnitMillis();
    return (thisDuration < otherDuration ? -1 : (thisDuration == otherDuration ? 0 : 1));
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
    DurationField lastRangeField = null;
    for (int i = 0; i < types.length; i++) {
        DateTimeFieldType loopType = types[i];
        DurationField loopUnitField = loopType.getDurationType().getField(iChronology);
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
        if (loopType.getRangeDurationType() != null) {
            lastRangeField = loopType.getRangeDurationType().getField(iChronology);
        }
    }
    iTypes = (DateTimeFieldType[]) types.clone();
    chronology.validate(this, values);
    iValues = (int[]) values.clone();
}

## Fixed Function 3
//-----------------------------------------------------------------------
public Partial with(DateTimeFieldType fieldType, int value) {
    if (fieldType == null) {
        throw new IllegalArgumentException("The field type must not be null");
    }
    int index = indexOf(fieldType);
    if (index == -1) {
        int[] newValues = new int[iTypes.length + 1];
        DateTimeFieldType[] newTypes = new DateTimeFieldType[iTypes.length + 1];
        int i;
        for (i = 0; i < iTypes.length; i++) {
            DateTimeFieldType loopType = iTypes[i];
            DurationField loopField = loopType.getDurationType().getField(iChronology);
            if (loopField.compareTo(fieldType.getDurationType().getField(iChronology)) > 0) {
                break;
            } else if (loopField.compareTo(fieldType.getDurationType().getField(iChronology)) == 0) {
                if ((loopType.getRangeDurationType() == null && fieldType.getRangeDurationType() == null) ||
                    (loopType.getRangeDurationType() != null && fieldType.getRangeDurationType() == null) ||
                    (loopType.getRangeDurationType() != null && fieldType.getRangeDurationType() != null && loopType.getRangeDurationType().getField(iChronology).compareTo(fieldType.getRangeDurationType().getField(iChronology)) > 0)) {
                    break;
                }
            }
        }
        int newPos = i;
        newTypes[newPos] = fieldType;
        newValues[newPos] = value;
        for (; i < iTypes.length; i++) {
            newTypes[i + 1] = iTypes[i];
            newValues[i + 1] = iValues[i];
        }
        // create new object with new values
        return new Partial(newTypes, newValues, iChronology);
    }
    else {
        int[] newValues = getField(index).set(this, index, iValues, value);
        return new Partial(this, newValues);
    }
}