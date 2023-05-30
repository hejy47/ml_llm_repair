public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, int secondOfMinute, int millisOfSecond) throws IllegalArgumentException {
    Chronology base;
    if ((base = getBase()) != null) {
        return base.getDateTimeMillis(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }
    // Assume date is Gregorian.
    long instant;
    if (year > 1582 || (year == 1582 && (monthOfYear > 10 || (monthOfYear == 10 && dayOfMonth >= 15)))) {
        instant = iGregorianChronology.getDateTimeMillis(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
        if (instant < iCutoverMillis) {
            // Maybe it's Julian.
            instant = iJulianChronology.getDateTimeMillis(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
            if (instant >= iCutoverMillis) {
                // Okay, it's in the illegal cutover gap.
                throw new IllegalArgumentException("Specified date does not exist");
            }
        }
    } else {
        // Date is in the Julian calendar, which did not have a 10-day gap in 1582.
        instant = iJulianChronology.getDateTimeMillis(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }
    return instant;
}