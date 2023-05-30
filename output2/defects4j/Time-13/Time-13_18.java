## Fixed Function 1
public int calculatePrintedLength(ReadablePeriod period, Locale locale) {
    long valueLong = getFieldValue(period);
    if (valueLong == Long.MAX_VALUE) {
        return 0;
    }
    int sum = Math.max(FormatUtils.calculateDigitCount(valueLong), iMinPrintedDigits);
    if (iFieldType >= SECONDS_MILLIS) {
        // valueLong contains the seconds and millis fields
        // the minimum output is 0.000, which is 4 or 5 digits with a negative
        sum = Math.max(sum, 5); // fixed, minimum output is 0.000 (5 digits including negative)
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
    int minDigits = iMinPrintedDigits;
    if (iPrefix != null) {
        iPrefix.printTo(buf, valueLong);
        valueLong = Math.abs(valueLong);
    }
    if (iFieldType >= SECONDS_MILLIS) {
        int seconds = (int)(valueLong / DateTimeConstants.MILLIS_PER_SECOND);
        valueLong -= seconds * DateTimeConstants.MILLIS_PER_SECOND;
        if (iFieldType == SECONDS_OPTIONAL_MILLIS) {
            if (valueLong == 0) {
                // remove the decimal point and fractional second digits
                FormatUtils.appendPaddedInteger(buf, seconds, 1);
                return;
            } else {
                buf.append(seconds);
                buf.append('.');
                FormatUtils.appendPaddedInteger(buf, valueLong, 3);
            }
        } else {
            buf.append(seconds);
            buf.append('.');
            FormatUtils.appendPaddedInteger(buf, valueLong, 3);
        }
    } else {
        FormatUtils.appendPaddedInteger(buf, (int) valueLong, minDigits <= 1 ? 1 : minDigits);
    }
    if (iSuffix != null) {
        iSuffix.printTo(buf, valueLong);
    }
}