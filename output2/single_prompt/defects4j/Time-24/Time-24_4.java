## Fixed Function 1
public long computeMillis(boolean resetFields, String text) {
    SavedField[] savedFields = iSavedFields;
    int count = iSavedFieldsCount;
    if (iSavedFieldsShared) {
        iSavedFields = savedFields = (SavedField[]) iSavedFields.clone();
        iSavedFieldsShared = false;
    }
    sort(savedFields, count);
    if (count > 0) {
        // alter base year for parsing if first field is month or day
        DurationField months = DurationFieldType.months().getField(iChrono);
        DurationField days = DurationFieldType.days().getField(iChrono);
        DurationField first = savedFields[0].iField.getDurationField();
        if (compareReverse(first.getDurationFieldType(), months.getDurationFieldType()) >= 0 
                && compareReverse(first.getDurationFieldType(), days.getDurationFieldType()) <= 0) { // fix condition
            saveField(DateTimeFieldType.year(), iDefaultYear);
            return computeMillis(resetFields, text);
        }
    }
    long millis = iMillis;
    try {
        for (int i = 0; i < count; i++) {
            millis = savedFields[i].set(millis, resetFields);
        }
    } catch (IllegalFieldValueException e) {
        if (text != null) {
            e.prependMessage("Cannot parse \"" + text + '"');
        }
        throw e;
    }
    if (iZone == null) {
        millis -= iOffset;
    } else {
        int offset = iZone.getOffsetFromLocal(millis);
        millis -= offset;
        if (offset != iZone.getOffset(millis)) {
            String message = "Illegal instant due to time zone offset transition (" + iZone + ')';
            if (text != null) {
                message = "Cannot parse \"" + text + "\": " + message;
            }
            throw new IllegalArgumentException(message);
        }
    }
    return millis;
}

## Fixed Function 2
private static int compareReverse(DurationField field1, DurationField field2) {
    if (field1 == null || !field1.isSupported()) {
        if (field2 == null || !field2.isSupported()) {
            return 0;
        }
        return -1;
    } else if (field2 == null || !field2.isSupported()) {
        return 1;
    }
    return -field1.compareTo(field2);
}