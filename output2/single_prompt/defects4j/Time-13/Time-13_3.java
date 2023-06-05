## Fixed Function 1
public int calculatePrintedLength(ReadablePeriod period, Locale locale) {
    long valueLong = getFieldValue(period);
    if (valueLong == Long.MAX_VALUE) {
        return 0;
    }
    int sum = Math.max(FormatUtils.calculateDigitCount(valueLong), iMinPrintedDigits);
    if (iFieldType >= SECONDS_MILLIS) {
        // valueLong contains the seconds and milliseconds fields
        // the minimum output is 0.000, which is 4 or 5 digits with a negative
        sum = Math.max(sum, 5); // Changed 4 to 5 to include negative sign
        // plus one for the decimal point
        sum++;
        if (iFieldType == SECONDS_OPTIONAL_MILLIS && (Math.abs(valueLong) % DateTimeConstants.MILLIS_PER_SECOND) == 0) {
            // remove three digits and decimal point
            sum -= 4;
        }
        // reset valueLong to refer to the seconds part for the prefix/suffix calculation
        valueLong = valueLong / DateTimeConstants.MILLIS_PER_SECOND;
    }
    int value = (int) valueLong;
    if (iPrefix != null) {
        sum += iPrefix.calculatePrintedLength(value);
    }
    if (iSuffix != null) {
        sum += iSuffix.calculatePrintedLength(value);
    }
    return sum;
}

## Fixed Function 2
public void printTo(StringBuffer buf, ReadablePeriod period, Locale locale) {
    long valueLong = getFieldValue(period);
    if (valueLong == Long.MAX_VALUE) {
        return;
    }
    int value = (int) valueLong;
    if (iFieldType >= SECONDS_MILLIS) {
        value = (int) (valueLong / DateTimeConstants.MILLIS_PER_SECOND);
    }
    if (iPrefix != null) {
        iPrefix.printTo(buf, value);
    }
    int minDigits = iMinPrintedDigits;
    // Handle negative values
    if (value < 0) {
        buf.append('-');
        value = -value;
        minDigits--;
    }
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
        iSuffix.printTo(buf, value);
    }
}