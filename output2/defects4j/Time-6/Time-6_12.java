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
            chronos = new ArrayList<>(2);
            cCache.put(zone, chronos);
        } else {
            for (int i = chronos.size() - 1; i >= 0; i--) {
                chrono = chronos.get(i);
                if (minDaysInFirstWeek == chrono.getMinimumDaysInFirstWeek() && cutoverInstant.equals(chrono.getGregorianCutover())) {
                    return chrono;
                }
            }
        }
        if (zone == DateTimeZone.UTC) {
            chrono = new GJChronology(JulianChronology.getInstance(zone, minDaysInFirstWeek), GregorianChronology.getInstance(zone, minDaysInFirstWeek), cutoverInstant);
        } else {
            chrono = getInstance(DateTimeZone.UTC, cutoverInstant, minDaysInFirstWeek);
            chrono = new GJChronology(ZonedChronology.getInstance(chrono, zone), chrono.getJulianChronology(), chrono.getGregorianChronology(), chrono.getCutoverInstant());
        }
        chronos.add(chrono);
    }
    return chrono;
}

## Fixed Function 2
public long add(long instant, int value) {
    if (instant >= iCutoverInstant) {
        // convert to local Gregorian time
        int gValue = iGregorianField.add(iZone.convertUTCToLocal(instant), value);
        // adjust for cutover
        if (iGregorianField.isLeapYear(gValue)) {
            if (instant >= iGapAdjusted) {
                if (instant + iGapDuration < iGregorianField.add(iCutoverInstant, value)) {
                    return iGregorianField.add(iJulianField.add(iGapAdjusted, 1), value);
                }
            } else {
                if (instant + iGapDuration >= iCutoverInstant) {
                    gValue = iGregorianField.add(gValue, -1);
                }
            }
        } else {
            if (instant >= iCutoverInstant && instant + iGapDuration < iGregorianField.add(iCutoverInstant, value)) {
                return iGregorianField.add(iJulianField.add(instant, 1), value);
            }
        }
        return iZone.convertLocalToUTC(iGregorianField.set(iZone.convertUTCToLocal(instant), gValue), false, instant);
    } else {
        // convert to local Julian time
        int jValue = iJulianField.add(iZone.convertUTCToLocal(instant), value);
        // adjust for cutover
        if (jValue >= JC_LIMIT) {
            // handle the rare case of crossing the gap when going backwards
            if (instant < iGapAdjusted) {
                instant = iGapAdjusted;
            }
            if (instant >= iGapAdjusted) {
                if (instant < iCutoverInstant) {
                    jValue = iJulianField.add(jValue, 1);
                } else {
                    long daysDifference = daysSinceGregorianTransition(instant);
                    if (daysDifference + 1 >= NOV_1582_ADJUST_JULIAN_DAY) {
                        jValue = iJulianField.add(jValue, 1);
                    }
                }
            }
        } else if (instant >= iGapAdjusted && instant < iCutoverInstant) {
            long daysDifference = daysSinceGregorianTransition(instant);
            if (daysDifference < NOV_1582_ADJUST_JULIAN_DAY) {
                jValue = iJulianField.add(jValue, 1);
            }
        }
        return iZone.convertLocalToUTC(iJulianField.set(iZone.convertUTCToLocal(instant), jValue), false, instant);
    }
}

## Fixed Function 3
public long add(long instant, long value) {
    if (instant >= iCutoverInstant) {
        // convert to local Gregorian time
        int localGregorian = iGregorianField.getMinimumValue();
        long end;
        int offset;
        if (instant + iGapDuration < iCutoverInstant) {
            // current zone and new zone
            localGregorian = iGregorianField.get(instant + iGapDuration);
            end = iZone.convertLocalToUTC(iCutoverInstant - iGapDuration, false, instant);
            offset = 0;
        } else {
            // current zone and new zone
            end = iGregorianField.add(iCutoverInstant, -1);
            offset = 1;
            if (instant < iGapAdjusted) {
                localGregorian = iJulianField.getMaximumValue();
            }
        }
        long local = iZone.convertUTCToLocal(end);
        int gValue = iGregorianField.add(localGregorian, value);
        if (gValue < localGregorian) {
            // Date is moving from a local time that occurred twice to a later wall time
            // adjust using the 7-day week
            long max = iZone.convertLocalToUTC(local + DateTimeConstants.MILLIS_PER_WEEK, false, end);
            if (max < instant) {
                return handleTransition(instant, iGapAdjusted, localGregorian);
            } else {
                return handleTransition(instant, max, gValue);
            }
        } else if (instant >= iGapAdjusted && instant < iCutoverInstant) {
            long daysDifference = daysSinceGregorianTransition(instant);
            if (daysDifference < NOV_1582_ADJUST_JULIAN_DAY) {
                // Before the Julian-Gregorian transition
                // (and after the previous Gregorian leap year if a leap year)
                // move to the next calendar date if still in the Julian calendar.
                if (localGregorian == iJulianField.getMaximumValue()) {
                    localGregorian = iJulianField.getMinimumValue();
                } else {
                    localGregorian = iJulianField.add(localGregorian, 1);
                }

                // ensure the current time zone offset is preserved
                long newInstant = iGregorianField.set(local, localGregorian);
                newInstant = iZone.convertLocalToUTC(newInstant, false, instant);
                if (newInstant < instant) {
                    newInstant = iZone.nextTransition(newInstant);
                }
                return newInstant;
            }
        } else {
            // handle the current zone, gap and new zone.
            if (instant >= iGapAdjusted && instant < iGapAdjusted + iGapDuration) {
                // within the gap
                if (iGapDuration == DateTimeConstants.MILLIS_PER_DAY && isLocalDateTimeGap(iZone, instant)) {
                    // if a 1-day gap and the local time is invalid, adjust using the 7-day week
                    long max = iZone.convertLocalToUTC(local + DateTimeConstants.MILLIS_PER_WEEK, false, end);
                    if (max < instant) {
                        return handleTransition(instant, iGapAdjusted, localGregorian);
                    } else {
                        return handleTransition(instant, max, gValue);
                    }
                } else {
                    return instant + iGapDuration;
                }
            }
        }
        return handleTransition(instant, end, gValue + offset);
    } else {
        // current zone and new zone
        int localJulian = iJulianField.getMinimumValue();
        long begin;
        int offset;
        if (instant < iGapAdjusted) {
            // current zone and new zone
            localJulian = iJulianField.get(iGapAdjusted - 1);
            begin = iZone.convertLocalToUTC(iGapAdjusted - iGapDuration, false, instant);
            offset = 0;
        } else {
            // current zone and new zone
            begin = iJulianField.set(this.iZone.convertUTCToLocal(instant), localJulian);
            offset = 1;
            if (instant >= iGapAdjusted + iGapDuration) {
                localJulian = iJulianField.getMaximumValue();
            }
        }
        long local = iZone.convertUTCToLocal(begin);
        int jValue = iJulianField.add(localJulian, value);
        if (jValue >= JC_LIMIT) {
            // Date is moving from the Julian calendar to the Gregorian calendar.
            // In the UK the day following 2nd Sep 1752 was 14th Sep 1752.
            // However, we'll assume that it should be the 3rd Sep 1752.
            // This should be acceptable, as any clients creating a date
            // on the 3rd Sep 1752 will expect the date to be in the Gregorian
            // calendar.
            long transition = gregorianToJulian(iCutoverInstant);
            if (instant >= transition) {
                // still Julian calendar
                if (jValue == JC_LIMIT) {
                    // Cross the gap
                    jValue = iJulianField.getMinimumValue();
                    long daysDifference = daysBetween(begin, iGapAdjusted);
                    local += daysDifference * DateTimeConstants.MILLIS_PER_DAY;
                    if (local + DateTimeConstants.MILLIS_PER_DAY < iZone.convertUTCToLocal(transition + DateTimeConstants.MILLIS_PER_DAY)) {
                        jValue = JC_LIMIT + 1;
                    }
                } else if (jValue <= JC_LIMIT + 1) {
                    jValue = JC_LIMIT + 1;
                }
            } else {
                // already in Gregorian calendar
                local += DateTimeConstants.MILLIS_PER_DAY;
            }
        } else if (instant >= iGapAdjusted && instant < iCutoverInstant - iGapDuration) {
            long daysDifference = daysSinceGregorianTransition(instant) + offset;
            if (daysDifference >= NOV_1582_ADJUST_JULIAN_DAY) {
                // On or after 15th Oct 1582.
                // (In the United Kingdom the switch was on 14th Sep 1752.)
                // Check if the current day (in Gregorian) existed in this calendar in the local time zone.
                long localEnd = local + (long) value;
                // Assume that no Gregorian time zone will put the transition time back into the Julian era!
                if (local >= -12219292800000L /*1582-10-15T00:00:00Z*/ && local < -11586048000000L /*1582-10-04T00:00:00Z*/) {
                    // no transition, before 4th Oct 1582 in Gregorian representation
                } else if (local >= -11586048000000L /*1582-10-04T00:00:00Z*/ && local < -11585984000000L /*1582-10-05T00:00:00Z*/) {
                    // lost 21st to 4th
                    if (jValue == 1) {
                        jValue = 2;
                    }
                } else if (local >= -11585984000000L /*1582-10-05T00:00:00Z*/ && local < -11578224000000L /*1582-10-15T00:00:00Z*/) {
                    // 8th to 15th
                } else if (local >= -11578224000000L /*1582-10-15T00:00:00Z*/ && local < -7843200000000L /*1583-03-01T00:00:00Z*/) {
                    // after 15th.
                    if (jValue >= 3) {
                        jValue += 10;
                    } else {
                        // gap where the 5th, 6th, 7th or 8th should be
                        if (localEnd >= -7843200000000L /*1583-03-01T00:00:00Z*/) {
                            jValue = 2;
                        }
                    }
                }
            } else {
                // before the Julian-Gregorian transition
                // (and after the previous Gregorian leap year if a leap year)
                // move to the next calendar date if still in the Julian calendar.
                if (jValue < JULIAN_ADJUSTED) {
                    jValue = JULIAN_ADJUSTED;
                }
                // ensure the current time zone offset is preserved
                long newInstant = iJulianField.set(local, jValue);
                newInstant = iZone.convertLocalToUTC(newInstant, false, instant);
                if (newInstant < instant) {
                    newInstant = iZone.nextTransition(newInstant);
                }
                return newInstant;
            }
        }
        return iZone.convertLocalToUTC(iJulianField.set(local, jValue), false, instant);
    }
}