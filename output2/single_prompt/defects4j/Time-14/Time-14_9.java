## Fixed Function 1
public int[] add(ReadablePartial partial, int fieldIndex, int[] values, int valueToAdd) {
    if (valueToAdd == 0) {
        return values;
    }
    if (DateTimeUtils.isContiguous(partial)) {
        long instant = partial.getChronology().set(partial, 0L);
        for (int i = 0, isize = partial.size(); i < isize; i++) {
            instant = partial.getFieldType(i).getField(partial.getChronology()).set(instant, values[i]);
        }
        instant = add(instant, valueToAdd);
        return partial.getChronology().get(partial, instant);
    } else {
        return super.add(partial, fieldIndex, values, valueToAdd);
    }
}

Changes Made:

- Changed the line: `instant = partial.getFieldType(i).getField(iChronology).set(instant, values[i]);` to `instant = partial.getFieldType(i).getField(partial.getChronology()).set(instant, values[i]);` as iChronology is not defined and to get Chronology use getChronology() method.
- Changed the line `return iChronology.get(partial, instant);` to `return partial.getChronology().get(partial, instant);` to get Chronology use getChronology() method.