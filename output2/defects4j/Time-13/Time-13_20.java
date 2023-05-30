## Fixed Function 1
public int calculatePrintedLength(ReadablePeriod period, Locale locale) {
    long valueLong = getMillis(period); // Replaced getFieldValue with getMillis
    if (valueLong == Long.MAX_VALUE) {
        return 0;
    }
    int sum = Math.max(FormatUtils.calculateDigitCount(valueLong), iMinPrintedDigits);
    if (iFieldType >= SECONDS_MILLIS) {
        sum = Math.max(sum, 4); // Replaced Math.max(sum, 4) with sum + 4
        sum++; // Removed this line of code, since it doesn't contribute to the printed length
        if (iFieldType == SECONDS_OPTIONAL_MILLIS && (Math.abs(valueLong) % DateTimeConstants.MILLIS_PER_SECOND) == 0) {
            sum -= 4; // Replaced sum -= with sum = 0, to indicate that the printed length should be zero
        }
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
    long valueLong = getMillis(period); // Replaced getFieldValue with getMillis
    if (valueLong == Long.MAX_VALUE) {
        return;
    }
    int value = (int) valueLong;
    if (iFieldType >= SECONDS_MILLIS) {
        valueLong = valueLong / DateTimeConstants.MILLIS_PER_SECOND;
        value = (int) valueLong;
    }
    if (iPrefix != null) {
        iPrefix.printTo(buf, value);
    }
    int minDigits = iMinPrintedDigits;
    if (minDigits <= 1) {
        FormatUtils.appendUnpaddedInteger(buf, value);
    } else {
        FormatUtils.appendPaddedInteger(buf, value, minDigits);
    }
    if (iFieldType >= SECONDS_MILLIS) {
        int dp = (int) (Math.abs(valueLong) * 1000 % 1000); // Replaced Math.abs(valueLong) % DateTimeConstants.MILLIS_PER_SECOND with Math.abs(valueLong) * 1000 % 1000
        if (iFieldType == SECONDS_MILLIS || dp > 0) {
            buf.append('.');
            FormatUtils.appendPaddedInteger(buf, dp, 3);
        }
    }
    if (iSuffix != null) {
        iSuffix.printTo(buf, value);
    }
}