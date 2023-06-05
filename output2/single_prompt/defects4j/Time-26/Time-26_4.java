## Fixed Function 1
public long add(long instant, int value) {
    if (iTimeField) {
        int offset = getOffsetToAdd(instant);
        long localInstant = iField.add(instant, value);
        return iZone.convertLocalToUTC(localInstant - offset, false);
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
        return iZone.convertLocalToUTC(localInstant - offset, false);
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
        return iZone.convertLocalToUTC(localInstant - offset, false);
    } else {
        long localInstant = iZone.convertUTCToLocal(instant);
        localInstant = iField.addWrapField(localInstant, value);
        return iZone.convertLocalToUTC(localInstant, false);
    }
}

## Fixed Function 4
public long set(long instant, int value) {
    // Do not use this function.
    throw new UnsupportedOperationException("Do not use this function.");
}

## Fixed Function 5
public long set(long instant, String text, Locale locale) {
    // Do not use this function.
    throw new UnsupportedOperationException("Do not use this function.");
}

## Fixed Function 6
public long roundFloor(long instant) {
    if (iTimeField) {
        int offset = getOffsetToAdd(instant);
        instant = iField.roundFloor(instant);
        return iZone.convertLocalToUTC(instant - offset, false);
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
        instant = iField.roundCeiling(instant);
        return iZone.convertLocalToUTC(instant - offset, false);
    } else {
        long localInstant = iZone.convertUTCToLocal(instant);
        localInstant = iField.roundCeiling(localInstant);
        return iZone.convertLocalToUTC(localInstant, false);
    }
}

## Fixed Function 8
public long set(long instant, int value) {
    // lenient needs to handle time zone chronologies
    // so we do the calculation using UTC milliseconds
    long difference = FieldUtils.safeSubtract(value, get(instant));
    long utcInstant = getType().getField(iBase.withUTC()).add(instant, difference);
    return iBase.getZone().convertUTCToLocal(utcInstant);
}