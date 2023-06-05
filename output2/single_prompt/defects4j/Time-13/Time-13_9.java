## Fixed Function 1
public int calculatePrintedLength(ReadablePeriod period, Locale locale) {
    long valueLong = period.toDurationFrom(DateTimeUtils.currentTimeMillis()).getMillis();
    if (valueLong == Long.MAX_VALUE) {
        return 0;
    }
    int sum = Math.max(FormatUtils.calculateDigitCount(valueLong), iMinPrintedDigits);
    if (iFieldType >= SECONDS_MILLIS) {
        // valueLong contains the seconds and millis fields
        // the minimum output is 0.000, which is 4 or 5 digits with a negative
        sum = Math.max(sum, 4);
        // plus one for the decimal point
        sum++;
        if (iFieldType == SECONDS_OPTIONAL_MILLIS && (Math.abs(valueLong) % DateTimeConstants.MILLIS_PER_SECOND) == 0) {
            // remove three digits and decimal point
            sum -= 4;
        }
        // reset valueLong to refer to the seconds part for the prefix/suffix calculation
        valueLong /= DateTimeConstants.MILLIS_PER_SECOND;
    }
    if (iPrefix != null) {
        sum += iPrefix.calculatePrintedLength(period, locale);
    }
    if (iSuffix != null) {
        sum += iSuffix.calculatePrintedLength(period, locale);
    }
    return sum;
}

## Fixed Function 2
public void printTo(StringBuffer buf, ReadablePeriod period, Locale locale) {
    long valueLong = period.toDurationFrom(DateTimeUtils.currentTimeMillis()).getMillis();
    if (valueLong == Long.MAX_VALUE) {
        return;
    }
    int value = (int) valueLong;
    if (iFieldType >= SECONDS_MILLIS) {
        value = (int) (valueLong / DateTimeConstants.MILLIS_PER_SECOND);
    }
    if (iPrefix != null) {
        iPrefix.printTo(buf, period, locale);
    }
    int minDigits = iMinPrintedDigits;
    if (minDigits <= 1) {
        FormatUtils.appendUnpaddedInteger(buf, value);
    } else {
        FormatUtils.appendPaddedInteger(buf, value, minDigits);
    }
    if (iFieldType >= SECONDS_MILLIS) {
        int dp = (int) (Math.abs(valueLong) % DateTimeConstants.MILLIS_PER_SECOND);
        if (iFieldType == SECONDS_MILLIS || dp > 0) {
            buf.append('.');
            FormatUtils.appendPaddedInteger(buf, dp, 3);
        }
    }
    if (iSuffix != null) {
        iSuffix.printTo(buf, period, locale);
    }
}