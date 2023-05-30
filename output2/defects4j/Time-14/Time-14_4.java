## Fixed Function 1
public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
    // overridden as superclass algorithm can't handle
    // 2004-02-29 + 48 months -> 2008-02-29 type dates
    if (valueToAdd == 0) {
        return values;
    }
    // month is largest field and being added to, such as month-day
    if (DateTimeUtils.isContiguous(partial)) {
        long instant = 0L;
        for (int i = 0, isize = partial.size(); i < isize; i++) {
            instant = partial.getFieldType(i).getField(iChronology).set(instant, values[i]);
        }
        instant = add(instant, valueToAdd, partial);
        return iChronology.get(partial, instant);
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}

## Fixed Function 2
private long add(long instant, int valueToAdd, ReadablePartial partial) {
    int[] values = partial.getValues();
    DurationField[] fields = new DurationField[partial.size()];

    for (int i = 0, isize = values.length; i < isize; i++) {
        fields[i] = partial.getFieldType(i).getField(iChronology);
    }

    for (int i = fields.length - 1; i >= 0 && valueToAdd > 0; i--) {
        long newValue = fields[i].add(instant, valueToAdd);
        // bug fix: using getDifference() with original value instead of current value
        valueToAdd -= fields[i].getDifference(newValue, instant);
        instant = newValue;
    }

    for (int i = fields.length - 1; i >= 0 && valueToAdd < 0; i--) {
        long newValue = fields[i].add(instant, valueToAdd);
        // bug fix: using getDifference() with original value instead of current value
        valueToAdd -= fields[i].getDifference(newValue, instant);
        instant = newValue;
    }

    return instant;
}