## Fixed Function 1
public int calculatePrintedLength(ReadablePeriod period, Locale locale) {
    long valueLong = getFieldValue(period);
    if (valueLong == Long.MAX_VALUE) {
        return 0;
    }
    int sum = iMinPrintedDigits; // initialize sum with minimum printed digits
    int digitCount = FormatUtils.calculateDigitCount(valueLong);
    if (digitCount > sum) {
        sum = digitCount; // update sum with actual digit count if it's greater than minimum printed digits
    }
    if (iFieldType >= SECONDS_MILLIS) {
        // valueLong contains the seconds and millis fields
        // the minimum output is 0.000, which is 4 or 5 digits with a negative
        int minSum = 5;
        if (valueLong < 0) {
            minSum = 6;
        }
        sum = Math.max(sum, minSum);
        if (iFieldType == SECONDS_OPTIONAL_MILLIS && (Math.abs(valueLong) % DateTimeConstants.MILLIS_PER_SECOND) == 0) {
            // remove three digits and decimal point
            sum -= 4;
        } else {
            // plus one for the decimal point
            sum++;
        }
        // reset valueLong to refer to the seconds part for the prefix/suffix calculation
        valueLong = valueLong / DateTimeConstants.MILLIS_PER_SECOND;
    }
    if (iPrefix != null) {
        sum += iPrefix.calculatePrintedLength(valueLong);
    }
    if (iSuffix != null) {
        sum += iSuffix.calculatePrintedLength(valueLong);
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
    int dp = 0; // initialize decimal point value
    if (iFieldType >= SECONDS_MILLIS) {
        value = (int) (valueLong / DateTimeConstants.MILLIS_PER_SECOND);
        dp = (int) (Math.abs(valueLong) % DateTimeConstants.MILLIS_PER_SECOND);
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
        if (iFieldType == SECONDS_MILLIS || dp > 0) {
            buf.append('.');
            FormatUtils.appendPaddedInteger(buf, dp, 3);
        }
    }
    if (iSuffix != null) {
        iSuffix.printTo(buf, value);
    }
}