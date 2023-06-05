public long getDateTimeMillis(int year, int monthOfYear, int dayOfMonth, int hourOfDay, int minuteOfHour, int secondOfMinute, int millisOfSecond) throws IllegalArgumentException {
    Chronology base;
    if ((base = getBase()) != null) {
        return base.getDateTimeMillis(year, monthOfYear, dayOfMonth, hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    }
    // Assume date is Gregorian.
    long instant;
    instant = gregorianToFixed(year, monthOfYear, dayOfMonth, isLeapYear(year));
    
    if (instant < iCutoverMillis) {
        // Maybe it's Julian.
        instant = julianToFixed(year, monthOfYear, dayOfMonth, isLeapYear(year));
        if (instant >= iCutoverMillis) {
            // Okay, it's in the illegal cutover gap.
            throw new IllegalArgumentException("Specified date does not exist");
        }
    }
    instant += timeToMillis(hourOfDay, minuteOfHour, secondOfMinute, millisOfSecond);
    return instant;
}

private boolean isLeapYear(int year) {
    return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
}

private long gregorianToFixed(int year, int monthOfYear, int dayOfMonth, boolean isLeapYear) {
    int monthDays = DAYS_IN_MONTH[monthOfYear - 1];
    if (monthOfYear > 2 && isLeapYear) {
        monthDays++;
    }
    return ((long) YEAR_DAYS[year - 1] + (long) DAY_OF_MONTH[monthOfYear - 1] + (long) dayOfMonth - 1L) + (isLeapYear && monthOfYear > 2 ? 1L : 0L);
}

private long julianToFixed(int year, int monthOfYear, int dayOfMonth, boolean isLeapYear) {
    int monthDays = JULIAN_DAYS_IN_MONTH[monthOfYear - 1];
    if (monthOfYear > 2 && isLeapYear) {
        monthDays++;
    }
    return ((long) JULIAN_YEAR_DAYS[year - 1] + (long) JULIAN_DAY_OF_MONTH[monthOfYear - 1] + (long) dayOfMonth - 1L) + (isLeapYear && monthOfYear > 2 ? 1L : 0L);
}

private long timeToMillis(int hourOfDay, int minuteOfHour, int secondOfMinute, int millisOfSecond) {
    long totalMillis = ((hourOfDay * 60L + minuteOfHour) * 60L + secondOfMinute) * 1000L + millisOfSecond;
    if (totalMillis < 0) {
        throw new IllegalArgumentException("Invalid time: " + hourOfDay + ":" + minuteOfHour + ":" + secondOfMinute + "." + millisOfSecond);
    }
    if (totalMillis >= MILLIS_PER_DAY) {
        throw new IllegalArgumentException("Invalid time: " + hourOfDay + ":" + minuteOfHour + ":" + secondOfMinute + "." + millisOfSecond);
    }
    return totalMillis;
}