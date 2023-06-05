## Fixed Function 1
public int compareTo(DurationField durationField) {
    if (this == durationField) {
        return 0;
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
        DurationFieldType durationType = loopType.getDurationType();
        if (durationType == null) {
            throw new IllegalArgumentException("Field '" + loopType.getName() + "' does not have a duration type");
        }
        DurationField loopUnitField = durationType.getField(iChronology);
        if (loopUnitField == null) {
            throw new IllegalArgumentException("The field '" + loopType.getName() + "' is not supported by the chronology '" + iChronology.getName() + "'");
        }
        if (i > 0) {
            int compare = lastUnitField.compareTo(loopUnitField);
            if (compare < 0 || (compare != 0 && !loopUnitField.isSupported())) {
                throw new IllegalArgumentException("Types array must be in order largest-smallest: " + types[i - 1].getName() + " < " + loopType.getName());
            } else if (compare == 0) {
                DurationFieldType lastRangeType = types[i - 1].getRangeDurationType();
                DurationFieldType rangeType = loopType.getRangeDurationType();
                if (lastRangeType == null) {
                    if (rangeType == null) {
                        throw new IllegalArgumentException("Types array must not contain duplicate: " + types[i - 1].getName() + " and " + loopType.getName());
                    }
                } else {
                    if (rangeType == null) {
                        throw new IllegalArgumentException("The field '" + loopType.getName() + "' does not have a range duration field but the field '" + types[i - 1].getName() + "' does");
                    }
                    DurationField lastRangeField = lastRangeType.getField(iChronology);
                    DurationField loopRangeField = rangeType.getField(iChronology);
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
        DurationFieldType durationType = fieldType.getDurationType();
        DurationField unitField = null;
        if (durationType != null) {
            unitField = durationType.getField(iChronology);
        }
        if (unitField != null && unitField.isSupported()) {
            for (; i < iTypes.length; i++) {
                DateTimeFieldType loopType = iTypes[i];
                DurationFieldType loopDurationType = loopType.getDurationType();
                if (loopDurationType == null) {
                    throw new IllegalArgumentException("Field '" + loopType.getName() + "' does not have a duration type");
                }
                DurationField loopUnitField = loopDurationType.getField(iChronology);
                if (loopUnitField == null) {
                    throw new IllegalArgumentException("The field '" + loopType.getName() + "' is not supported by the chronology '" + iChronology.getName() + "'");
                }
                if (loopUnitField.isSupported()) {
                    int compare = unitField.compareTo(loopUnitField);
                    if (compare > 0) {
                        break;
                    } else if (compare == 0) {
                        DurationFieldType lastRangeType = i > 0 ? iTypes[i - 1].getRangeDurationType() : null;
                        DurationFieldType rangeType = fieldType.getRangeDurationType();
                        if (lastRangeType == null) {
                            if (rangeType == null) {
                                throw new IllegalArgumentException("Types array must not contain duplicate: " + fieldType.getName() + " and " + loopType.getName());
                            }
                        } else {
                            if (rangeType == null) {
                                throw new IllegalArgumentException("The field '" + loopType.getName() + "' does not have a range duration field but the field '" + fieldType.getName() + "' does");
                            }
                            DurationField lastRangeField = lastRangeType.getField(iChronology);
                            DurationField loopRangeField = rangeType.getField(iChronology);
                            if (lastRangeField.compareTo(loopRangeField) > 0) {
                                break;
                            }
                            if (lastRangeField.compareTo(loopRangeField) == 0) {
                                throw new IllegalArgumentException("Types array must not contain duplicate: " + fieldType.getName() + " and " + loopType.getName());
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