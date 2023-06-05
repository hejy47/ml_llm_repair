## Fixed Function 1
public int compareTo(DurationField durationField) {
    if (durationField instanceof PreciseDurationField) {
        PreciseDurationField other = (PreciseDurationField) durationField;
        long thisMillis = getUnitMillis();
        long otherMillis = other.getUnitMillis();
        if (thisMillis < otherMillis) {
            return -1;
        } else if (thisMillis > otherMillis) {
            return 1;
        }
    }
    return (getUnitMillis() == durationField.getUnitMillis() ? 0 :
            (getUnitMillis() < durationField.getUnitMillis() ? -1 : 1));
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
    DurationFieldType lastUnitFieldType = null;
    for (int i = 0; i < types.length; i++) {
        DateTimeFieldType loopType = types[i];
        DurationFieldType loopUnitFieldType = loopType.getDurationType();
        if (i > 0) {
            int compare = lastUnitFieldType.compareTo(loopUnitFieldType);
            if (compare < 0 || (compare != 0 && loopUnitFieldType.isSupported() == false)) {
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
                    DurationFieldType lastRangeFieldType = types[i - 1].getRangeDurationType();
                    DurationFieldType loopRangeFieldType = loopType.getRangeDurationType();
                    if (lastRangeFieldType.compareTo(loopRangeFieldType) < 0) {
                        throw new IllegalArgumentException("Types array must be in order largest-smallest: " + types[i - 1].getName() + " < " + loopType.getName());
                    }
                    if (lastRangeFieldType.compareTo(loopRangeFieldType) == 0) {
                        throw new IllegalArgumentException("Types array must not contain duplicate: " + types[i - 1].getName() + " and " + loopType.getName());
                    }
                }
            }
        }
        lastUnitFieldType = loopUnitFieldType;
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
    int index = indexOfSupported(fieldType);
    if (index == -1) {
        index = indexOf(fieldType);
        if (index == -1) {
            DateTimeFieldType[] newTypes = new DateTimeFieldType[iTypes.length + 1];
            int[] newValues = new int[newTypes.length];
            // find correct insertion point to keep largest-smallest order
            int i = 0;
            DurationFieldType unitFieldType = fieldType.getDurationType();
            for (; i < iTypes.length; i++) {
                DateTimeFieldType loopType = iTypes[i];
                DurationFieldType loopUnitFieldType = loopType.getDurationType();
                if (loopUnitFieldType.isSupported()) {
                    int compare = unitFieldType.compareTo(loopUnitFieldType);
                    if (compare > 0) {
                        break;
                    } else if (compare == 0) {
                        DurationFieldType rangeFieldType = fieldType.getRangeDurationType();
                        DurationFieldType loopRangeFieldType = loopType.getRangeDurationType();
                        if (rangeFieldType.compareTo(loopRangeFieldType) > 0) {
                            break;
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
    }
    if (value == getValue(index)) {
        return this;
    }
    int[] newValues = getValues();
    newValues = getField(index).set(this, index, newValues, value);
    return new Partial(this, newValues);
}