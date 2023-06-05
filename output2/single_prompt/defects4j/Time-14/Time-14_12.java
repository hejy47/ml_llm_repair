## Fixed Function 1
public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
    if (valueToAdd == 0) {
        return values;
    }
    // month is largest field and being added to, such as month-day
    if (DateTimeUtils.isContiguous(partial)) {
        long instant = 0L;
        for (int i = 0, isize = partial.size(); i < isize; i++) {
            instant = partial.getFieldType(i).getField(iChronology).set(instant, values[i]);
        }
        instant = addFieldValue(instant, fieldIndex, valueToAdd);
        return iChronology.get(partial, instant);
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}

// added helper function below
private long addFieldValue(long instant, int fieldIndex, int valueToAdd) {
    // handle field overflow
    int maxVal = iChronology.getMaximumValue(partial.getFieldType(fieldIndex));
    int minVal = iChronology.getMinimumValue(partial.getFieldType(fieldIndex));
    int curVal = iChronology.get(partial, instant, fieldIndex);
    long result = instant;
    if ((valueToAdd >= maxVal - curVal) || (valueToAdd <= minVal - curVal)) {
        result = iChronology.add(instant, fieldIndex, valueToAdd);
    } else {
        result = iChronology.set(instant, fieldIndex, curVal + valueToAdd);
    }
    return result;
}