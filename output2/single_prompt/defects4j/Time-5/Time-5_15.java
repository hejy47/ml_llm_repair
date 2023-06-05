## Fixed Function 1

//-----------------------------------------------------------------------
public Period normalizedStandard(PeriodType type) {
    type = DateTimeUtils.getPeriodType(type);
    // no overflow can happen, even with Integer.MAX_VALUEs
    long millis = getMillis();
    millis += (((long) getSeconds()) * ((long) DateTimeConstants.MILLIS_PER_SECOND));
    millis += (((long) getMinutes()) * ((long) DateTimeConstants.MILLIS_PER_MINUTE));
    millis += (((long) getHours()) * ((long) DateTimeConstants.MILLIS_PER_HOUR));
    millis += (((long) getDays()) * ((long) DateTimeConstants.MILLIS_PER_DAY));
    millis += (((long) getWeeks()) * ((long) DateTimeConstants.MILLIS_PER_WEEK));
    int years = getYears();
    int months = getMonths();
    if (years != 0 || months != 0) {
        int totalMonths = years * 12 + months;
        millis += (((long) totalMonths) * ((long) DateTimeConstants.MILLIS_PER_MONTH));
    }
    Period result = new Period(millis, type, ISOChronology.getInstanceUTC());
    return result.normalizedStandard();
}