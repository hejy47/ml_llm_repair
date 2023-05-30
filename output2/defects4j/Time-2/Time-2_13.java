## Fixed Function 1
public int compareTo(DurationField durationField) {
    if (durationField instanceof PreciseDurationField) {
        PreciseDurationField other = (PreciseDurationField) durationField;
        long thisUnitMillis = getUnitMillis();
        long otherUnitMillis = other.getUnitMillis();
        if (thisUnitMillis == otherUnitMillis) {
            return 0;
        } else if (thisUnitMillis < otherUnitMillis) {
            return -1;
        } else {
            return 1;
        }
    }
    return getField().compareTo(durationField.getField());
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
        DurationFieldType fieldType = loopType.getDurationType();
        if (i > 0) {
            DurationField loopUnitField = fieldType.getField(iChronology);
            int compare = lastUnitField.compareTo(loopUnitField);
            if (compare < 0 || (compare != 0 && loopUnitField.isSupported() == false)) {
                throw new IllegalArgumentException("Types array must be in order largest-smallest: " + types[i - 1].getName() + " < " + loopType.getName());
            } else if (compare == 0) {
                DurationFieldType lastRangeType = types[i - 1].getRangeDurationType();
                DurationFieldType loopRangeType = loopType.getRangeDurationType();
                if (lastRangeType == null) {
                    if (loopRangeType == null) {
                        throw new IllegalArgumentException("Types array must not contain duplicate: " + types[i - 1].getName() + " and " + loopType.getName());
                    }
                } else {
                    if (loopRangeType == null) {
                        throw new IllegalArgumentException("Types array must be in order largest-smallest: " + types[i - 1].getName() + " < " + loopType.getName());
                    }
                    DurationField lastRangeField = lastRangeType.getField(iChronology);
                    DurationField loopRangeField = loopRangeType.getField(iChronology);
                    if (lastRangeField.compareTo(loopRangeField) < 0) {
                        throw new IllegalArgumentException("Types array must be in order largest-smallest: " + types[i - 1].getName() + " < " + loopType.getName());
                    }
                    if (lastRangeField.compareTo(loopRangeField) == 0) {
                        throw new IllegalArgumentException("Types array must not contain duplicate: " + types[i - 1].getName() + " and " + loopType.getName());
                    }
                }
            }
        }
        lastUnitField = fieldType.getField(iChronology);
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
        DateTimeFieldType[] newTypes = new DateTimeFieldType[iTypes.length + 1];
        int[] newValues = new int[newTypes.length];
        // find correct insertion point to keep largest-smallest order
        int i = 0;
        DurationField unitField = fieldType.getDurationType().getField(iChronology);
        if (unitField.isSupported()) {
            for (; i < iTypes.length; i++) {
                DateTimeFieldType loopType = iTypes[i];
                DurationFieldType loopFieldType = loopType.getDurationType();
                DurationField loopUnitField = loopFieldType.getField(iChronology);
                if (loopUnitField.isSupported()) {
                    int compare = unitField.compareTo(loopUnitField);
                    if (compare > 0) {
                        break;
                    } else if (compare == 0) {
                        DurationFieldType rangeFieldType = fieldType.getRangeDurationType();
                        DurationFieldType loopRangeType = loopType.getRangeDurationType();
                        if (rangeFieldType != null && loopRangeType != null) {
                            DurationField rangeField = rangeFieldType.getField(iChronology);
                            DurationField loopRangeField = loopRangeType.getField(iChronology);
                            if (rangeField.compareTo(loopRangeField) > 0) {
                                break;
                            }
                        }
                    }
                }
            }
        }
        System.arraycopy(iTypes, 0, newTypes, 0, i);
        System.arraycopy(iValues, 0, newValues, 0, i);
        newTypes[i] = fieldType;
        newValues[i] = value;
        System.arraycopy(iTypes, i, newTypes, i + 1, newTypes.length - i - 1);
        System.arraycopy(iValues, i, newValues, i + 1, newValues.length - i - 1);
        // use public constructor to ensure full validation
        // this isn't overly efficient, but is safe
        Partial newPartial = new Partial(newTypes, newValues, iChronology);
        iChronology.validate(newPartial, newValues);
        return newPartial;
    }
    if (value == getValue(index)) {
        return this;
    }
    int[] newValues = getValues();
    newValues = getField(index).set(this, index, newValues, value);
    return new Partial(this, newValues);
}