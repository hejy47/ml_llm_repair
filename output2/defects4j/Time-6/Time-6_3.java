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
            chrono = new GJChronology(JulianChronology.getInstance(zone, minDaysInFirstWeek), GregorianChronology.getInstance(zone, minDaysInFirstWeek), cutoverInstant);
            chronos.add(chrono);
            cCache.put(zone, chronos);
        } else {
            for (int i = chronos.size(); --i >= 0; ) {
                chrono = chronos.get(i);
                if (minDaysInFirstWeek == chrono.getMinimumDaysInFirstWeek() && cutoverInstant.equals(chrono.getGregorianCutover())) {
                    return chrono;
                }
            }
            chrono = new GJChronology(JulianChronology.getInstance(zone, minDaysInFirstWeek), GregorianChronology.getInstance(zone, minDaysInFirstWeek), cutoverInstant);
            chronos.add(chrono);
        }
    }
    return chrono;
}

## Fixed Function 2
public long add(long instant, int value) {
    if (instant >= iCutover) {
        instant = iGregorianField.add(instant, value);
        if (instant < iCutover) {
            // Only adjust if gap fully crossed.
            if (instant + iGapDuration >= iCutover) {
                // need to undo the addition + shift back (the addition was a negative value)
                instant = iGregorianField.add(instant - iGapDuration, -1);
            }
        }
    } else {
        instant = iJulianField.add(instant, value);
        if (instant >= iCutover) {
            // Only adjust if gap fully crossed.
            if (instant - iGapDuration < iCutover) {
                // need to undo the addition + shift forward (the addition was a negative value)
                instant = iJulianField.add(instant + iGapDuration, 1);
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
            if (instant + iGapDuration >= iCutover) {
                // need to undo the addition + shift back (the addition was a negative value)
                instant = iGregorianField.add(instant - iGapDuration, -1);
            }
        }
    } else {
        instant = iJulianField.add(instant, value);
        if (instant >= iCutover) {
            // Only adjust if gap fully crossed.
            if (instant - iGapDuration < iCutover) {
                // need to undo the addition + shift forward (the addition was a negative value)
                instant = iJulianField.add(instant + iGapDuration, 1);
            }
        }
    }
    return instant;
}