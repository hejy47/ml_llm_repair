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
        // changed from ArrayList to LinkedList for more efficient removal
        LinkedList<GJChronology> chronos = cCache.get(zone);
        if (chronos == null) {
            chronos = new LinkedList<GJChronology>();
            cCache.put(zone, chronos);
        } else {
            for (Iterator<GJChronology> it = chronos.iterator(); it.hasNext();) {
                chrono = it.next();
                if (minDaysInFirstWeek == chrono.getMinimumDaysInFirstWeek() && cutoverInstant.equals(chrono.getGregorianCutover())) {
                    return chrono;
                }
            }
        }
        if (zone == DateTimeZone.UTC) {
            chrono = new GJChronology(JulianChronology.getInstance(zone, minDaysInFirstWeek), GregorianChronology.getInstance(zone, minDaysInFirstWeek), cutoverInstant);
        } else {
            chrono = getInstance(DateTimeZone.UTC, cutoverInstant, minDaysInFirstWeek);
            chrono = new GJChronology(ZonedChronology.getInstance(chrono, zone), chrono.iJulianChronology, chrono.iGregorianChronology, chrono.iCutoverInstant);
        }
        chronos.addLast(chrono);
    }
    return chrono;
}

## Fixed Function 2
public long add(long instant, int value) {
    if (instant >= iCutover + iGapDuration) {
        // fixed condition to check for entire gap duration
        instant = iGregorianField.add(instant, value);
        if (instant < iCutover + iGapDuration && instant >= iCutover) {
            instant += iGapDuration;
        }
    } else if (instant < iCutover - iGapDuration) {
        // fixed condition to check for entire gap duration
        instant = iJulianField.add(instant, value);
        if (instant >= iCutover - iGapDuration && instant < iCutover) {
            instant -= iGapDuration;
        }
    } else {
        // inside the gap
        Chronology chrono = (instant >= iCutover ? iGregorianChronology : iJulianChronology);
        instant = chrono.add(instant, value);
        // if still in gap, adjust again
        if (isInGap(instant)) {
            long end = getGapDurationAfter(instant);
            int years = chrono.year().get(instant + end);
            int newYears = years + value;
            int gapYears = getGregorianYear(iCutover - 1) - 1;  // years in 1st year of gap
            if (newYears <= gapYears) {
                newYears = gapYears + 1;
            }
            int days = chrono.dayOfYear().get(instant + end);
            int newDays = days;
            int maxDays = chrono.daysOfYear(newYears, leapYear(newYears));
            if (newDays > maxDays) {
                newDays = maxDays;
            }
            instant = chrono.year().set(chrono.dayOfYear().set(instant + end, newDays), newYears);
        }
    }
    return instant;
}

## Fixed Function 3
public long add(long instant, long value) {
    if (instant >= iCutover + iGapDuration) {
        // fixed condition to check for entire gap duration
        instant = iGregorianField.add(instant, value);
        if (instant < iCutover + iGapDuration && instant >= iCutover) {
            instant += iGapDuration;
        }
    } else if (instant < iCutover - iGapDuration) {
        // fixed condition to check for entire gap duration
        instant = iJulianField.add(instant, value);
        if (instant >= iCutover - iGapDuration && instant < iCutover) {
            instant -= iGapDuration;
        }
    } else {
        // inside the gap
        Chronology chrono = (instant >= iCutover ? iGregorianChronology : iJulianChronology);
        instant = chrono.add(instant, value);
        // if still in gap, adjust again
        if (isInGap(instant)) {
            long end = getGapDurationAfter(instant);
            int years = chrono.year().get(instant + end);
            int newYears = years + (int) (value / (long) DateTimeConstants.MILLIS_PER_YEAR);
            int gapYears = getGregorianYear(iCutover - 1) - 1;  // years in 1st year of gap
            if (newYears <= gapYears) {
                newYears = gapYears + 1;
            }
            long durationYears = (newYears - years) * (long) DateTimeConstants.MILLIS_PER_YEAR;
            instant = chrono.add(instant, durationYears + end - instant);
            int days = chrono.dayOfYear().get(instant);
            int maxDays = chrono.daysOfYear(newYears, leapYear(newYears));
            if (days > maxDays) {
                instant = instant - (days - maxDays) * (long) DateTimeConstants.MILLIS_PER_DAY;
            }
        }
    }
    return instant;
}