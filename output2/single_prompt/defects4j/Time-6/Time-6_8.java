## Fixed Function 1
public static synchronized GJChronology getInstance(DateTimeZone zone, ReadableInstant gregorianCutover, int minDaysInFirstWeek) {
    zone = DateTimeUtils.getZone(zone);

    Instant cutoverInstant;
    if (gregorianCutover == null) {
        cutoverInstant = DEFAULT_CUTOVER;
    } else {
        cutoverInstant = gregorianCutover.toInstant();
    }

    GJChronology chrono;
    synchronized (cCache) {
        ArrayList<GJChronology> chronos = cCache.get(zone);
        if (chronos == null) {
            chronos = new ArrayList<GJChronology>(2);
            cCache.put(zone, chronos);
        } else {
            for (int i = chronos.size(); --i >= 0; ) {
                chrono = chronos.get(i);
                if (minDaysInFirstWeek == chrono.getMinimumDaysInFirstWeek() && cutoverInstant.equals(chrono.getGregorianCutover())) {
                    return chrono;
                }
            }
        }

        if (zone == DateTimeZone.UTC) {
            chrono = new GJChronology(JulianChronology.getInstance(zone, minDaysInFirstWeek), GregorianChronology.getInstance(zone, minDaysInFirstWeek), cutoverInstant);
        } else {
            // Fix: Use the provided zone
            //chrono = getInstance(DateTimeZone.UTC, cutoverInstant, minDaysInFirstWeek);
            chrono = getInstance(zone, cutoverInstant, minDaysInFirstWeek);
            chrono = new GJChronology(ZonedChronology.getInstance(chrono, zone), chrono.iJulianChronology, chrono.iGregorianChronology, chrono.iCutoverInstant);
        }
        chronos.add(chrono);
    }
    return chrono;
}

## Fixed Function 2
public long add(long instant, int value) {
    if (instant >= iCutover) {
        instant = iGregorianField.add(instant, value);
        if (instant < iCutover) {
            // Only adjust if gap fully crossed.
            // Fix: Use the correct variable name for the gap duration
            //if (instant + iGapDuration < iCutover) {
            if (instant + iGregorianAdvance < iCutover) {
                instant = gregorianToJulian(instant);
            }
        }
    } else {
        instant = iJulianField.add(instant, value);
        if (instant >= iCutover) {
            // Only adjust if gap fully crossed.
            // Fix: Use the correct variable name for the gap duration
            //if (instant - iGapDuration >= iCutover) {
            if (instant - iJulianAdvance >= iCutover) {
                // no special handling for year zero as cutover always after year zero
                instant = julianToGregorian(instant);
            }
        }
    }
    return instant;
}

## Fixed Function 3
public long add(long instant, long value) {
    if (instant >= iCutover) {
        instant = iGregorianField.add(instant, value);
        if (instant < iCutover) {
            // Only adjust if gap fully crossed.
            // Fix: Use the correct variable name for the gap duration
            //if (instant + iGapDuration < iCutover) {
            if (instant + iGregorianAdvance < iCutover) {
                instant = gregorianToJulian(instant);
            }
        }
    } else {
        instant = iJulianField.add(instant, value);
        if (instant >= iCutover) {
            // Only adjust if gap fully crossed.
            // Fix: Use the correct variable name for the gap duration
            //if (instant - iGapDuration >= iCutover) {
            if (instant - iJulianAdvance >= iCutover) {
                // no special handling for year zero as cutover always after year zero
                instant = julianToGregorian(instant);
            }
        }
    }
    return instant;
}