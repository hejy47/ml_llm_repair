## Fixed Function 1
public int compareTo(DurationField durationField) {
    if (this == durationField) {
        return 0;
    }
    if (durationField == null) {
        throw new NullPointerException();
    }
    DurationFieldType otherType = durationField.getType();
    if (getType().equals(otherType)) {
        return 0;
    }
    throw new ClassCastException("Invalid DurationField type: " + otherType);
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
    if (types.length == 1) {
        validate(types[0], values[0]);
        iTypes = types;
        iValues = values;
        return;
    }
    DateTimeFieldType[] typesCopy = (DateTimeFieldType[]) types.clone();
    int[] valuesCopy = (int[]) values.clone();
    PartialConverter converter = new PartialConverter();
    converter.convert(this, typesCopy, valuesCopy);
    iTypes = (DateTimeFieldType[]) typesCopy.clone();
    iValues = (int[]) valuesCopy.clone();
    chronology.validate(this, valuesCopy);
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
        int i = 0;
        DurationField unitField = fieldType.getDurationType().getField(iChronology);
        if (unitField.isSupported()) {
            for (; i < iTypes.length; i++) {
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
                        }
                    }
                }
            }
        }
        // Arraycopy elements before insert index
        System.arraycopy(iTypes, 0, newTypes, 0, i);
        System.arraycopy(iValues, 0, newValues, 0, i);
        // Insert new element
        newTypes[i] = fieldType;
        newValues[i] = value;
        // Arraycopy elements after insert index
        System.arraycopy(iTypes, i, newTypes, i + 1, iTypes.length - i);
        System.arraycopy(iValues, i, newValues, i + 1, iValues.length - i);
        return new Partial(newTypes, newValues, iChronology);
    }
    if (value == getValue(index)) {
        return this;
    }
    int[] newValues = (int[]) iValues.clone();
    newValues = getField(index).set(this, index, newValues, value);
    return new Partial(this, newValues);
}