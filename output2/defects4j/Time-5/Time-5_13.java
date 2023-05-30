## Fixed Function 1
//-----------------------------------------------------------------------
public Period normalizedStandard(PeriodType type) {
    type = DateTimeUtils.getPeriodType(type);
    // no overflow can happen, even with Integer.MAX_VALUEs
    long millis = getMillis() +
            ((long) getSeconds() * DateTimeConstants.MILLIS_PER_SECOND) +
            ((long) getMinutes() * DateTimeConstants.MILLIS_PER_MINUTE) +
            ((long) getHours() * DateTimeConstants.MILLIS_PER_HOUR) +
            ((long) getDays() * DateTimeConstants.MILLIS_PER_DAY) +
            ((long) getWeeks() * DateTimeConstants.MILLIS_PER_WEEK);
    Period result = new Period(millis, type, ISOChronology.getInstanceUTC());
    int years = getYears();
    int months = getMonths();
    if (years != 0 || months != 0) {
        years = FieldUtils.safeAdd(years, months / 12);
        months = months % 12;
        if (years != 0) {
            result = result.withYears(years);
        }
        if (months != 0) {
            result = result.withMonths(months);
        }
    }
    return result;
}