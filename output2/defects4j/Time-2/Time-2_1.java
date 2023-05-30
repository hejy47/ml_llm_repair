## Fixed Function 1
public int compareTo(DurationField durationField) {
    if (durationField instanceof UnsupportedDurationField) {
        return 1;
    }
    DurationField other = (DurationField) durationField;
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
    DurationFieldType currentRangeType = null;
    DurationField currentRangeDurationField = null;
    for (int i = 0; i < types.length; i++) {
        DateTimeFieldType loopType = types[i];
        DurationField loopDurationField = loopType.getDurationType().getField(iChronology);
        if (i > 0) {
            if (currentRangeType == null && currentRangeDurationField == null) {
                currentRangeType = types[i - 1].getRangeDurationType();
                if (currentRangeType != null) {
                    currentRangeDurationField = currentRangeType.getField(iChronology);
                }
            }
            if (currentRangeType != null) {
                DurationField loopRangeDurationField = loopType.getRangeDurationType().getField(iChronology);
                if (currentRangeDurationField.compareTo(loopRangeDurationField) < 0) {
                    throw new IllegalArgumentException("Types array must be in order largest-smallest: " + types[i - 1].getName() + " < " + loopType.getName());
                } else if (currentRangeDurationField.compareTo(loopRangeDurationField) == 0) {
                    if (currentRangeType.compareTo(loopType.getRangeDurationType()) < 0) {
                        throw new IllegalArgumentException("Types array must be in order largest-smallest: " + types[i - 1].getName() + " < " + loopType.getName());
                    } else if (currentRangeType.compareTo(loopType.getRangeDurationType()) == 0) {
                        throw new IllegalArgumentException("Types array must not contain duplicate: " + types[i - 1].getName() + " and " + loopType.getName());
                    }
                }
                currentRangeType = loopType.getRangeDurationType();
                if (currentRangeType == null) {
                    currentRangeDurationField = null;
                } else {
                    currentRangeDurationField = currentRangeType.getField(iChronology);
                }
            } else {
                int compare = currentRangeDurationField.compareTo(loopDurationField);
                if (compare < 0) {
                    throw new IllegalArgumentException("Types array must be in order largest-smallest: " + types[i - 1].getName() + " < " + loopType.getName());
                } else if (compare == 0) {
                    throw new IllegalArgumentException("Types array must not contain duplicate: " + types[i - 1].getName() + " and " + loopType.getName());
                }
            }
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
        DateTimeFieldType[] newTypes = new DateTimeFieldType[iTypes.length + 1];
        int[] newValues = new int[newTypes.length];
        // find correct insertion point to keep largest-smallest order
        int i = 0;
        DurationField unitField = fieldType.getDurationType().getField(iChronology);
        if (unitField.isSupported()) {
            int len = iTypes.length;
            for (; i < len; i++) {
                DateTimeFieldType loopType = iTypes[i];
                DurationField loopUnitField = loopType.getDurationType().getField(iChronology);
                if (loopUnitField.isSupported()) {
                    int compare = unitField.compareTo(loopUnitField);
                    if (compare > 0) {
                        break;
                    } else if (compare == 0) {
                        DurationField rangeField = fieldType.getRangeDurationType().getField(iChronology);
                        DurationField loopRangeField = loopType.getRangeDurationType().getField(iChronology);
                        if (rangeField.compareTo(loopRangeField) > 0) {
                            break;
                        } else if (rangeField.compareTo(loopRangeField) == 0) {
                            throw new IllegalArgumentException("Types array must not contain duplicate: " + fieldType.getName() + " and " + loopType.getName());
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