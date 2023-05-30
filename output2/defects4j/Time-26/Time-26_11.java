## Fixed Function 1
public long add(long instant, int value) {
    if (iTimeField) {
        int offset = getOffsetToAdd(instant);
        long localInstant = iField.add(instant, value);
        return iZone.convertLocalToUTC(localInstant, false) - offset;
    } else {
        long localInstant = iZone.convertUTCToLocal(instant);
        localInstant = iField.add(localInstant, value);
        return iZone.convertLocalToUTC(localInstant, false);
    }
}

## Fixed Function 2
public long add(long instant, long value) {
    if (iTimeField) {
        int offset = getOffsetToAdd(instant);
        long localInstant = iField.add(instant, value);
        return iZone.convertLocalToUTC(localInstant, false) - offset;
    } else {
        long localInstant = iZone.convertUTCToLocal(instant);
        localInstant = iField.add(localInstant, value);
        return iZone.convertLocalToUTC(localInstant, false);
    }
}

## Fixed Function 3
public long addWrapField(long instant, int value) {
    if (iTimeField) {
        int offset = getOffsetToAdd(instant);
        long localInstant = iField.addWrapField(instant, value);
        return iZone.convertLocalToUTC(localInstant, false) - offset;
    } else {
        long localInstant = iZone.convertUTCToLocal(instant);
        localInstant = iField.addWrapField(localInstant, value);
        return iZone.convertLocalToUTC(localInstant, false);
    }
}

## Fixed Function 4
public long set(long instant, int value) {
    long localInstant = iZone.convertUTCToLocal(instant);
    localInstant = iField.set(localInstant, value);
    long result = iZone.convertLocalToUTC(localInstant, false);
    if (get(result) != value) {
        throw new IllegalFieldValueException(iField.getType(), new Integer(value), "Illegal instant due to time zone offset transition: " + DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS").withZone(iZone).print(new Instant(localInstant)) + " (" + iZone.getID() + ")");
    }
    return result;
}

## Fixed Function 5
public long set(long instant, String text, Locale locale) {
    // cannot verify that new value stuck because set may be lenient
    long localInstant = iZone.convertUTCToLocal(instant);
    localInstant = iField.set(localInstant, text, locale);
    return iZone.convertLocalToUTC(localInstant, false);
}

## Fixed Function 6
public long roundFloor(long instant) {
    if (iTimeField) {
        int offset = getOffsetToAdd(instant);
        instant = iField.roundFloor(instant + offset);
        return iZone.convertLocalToUTC(instant, false) - offset;
    } else {
        long localInstant = iZone.convertUTCToLocal(instant);
        localInstant = iField.roundFloor(localInstant);
        return iZone.convertLocalToUTC(localInstant, false);
    }
}

## Fixed Function 7
public long roundCeiling(long instant) {
    if (iTimeField) {
        int offset = getOffsetToAdd(instant);
        instant = iField.roundCeiling(instant + offset);
        return iZone.convertLocalToUTC(instant, false) - offset;
    } else {
        long localInstant = iZone.convertUTCToLocal(instant);
        localInstant = iField.roundCeiling(localInstant);
        return iZone.convertLocalToUTC(localInstant, false);
    }
}

## Fixed Function 8
public long set(long instant, int value) {
    // lenient needs to handle time zone chronologies
    // so we do the calculation using local milliseconds
    long localInstant = iBase.getZone().convertUTCToLocal(instant);
    long difference = FieldUtils.safeSubtract(value, get(instant));
    localInstant = getType().getField(iBase.getChronology()).add(localInstant, difference);
    return iBase.getZone().convertLocalToUTC(localInstant, false);
}