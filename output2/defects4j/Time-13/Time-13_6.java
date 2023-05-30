## Fixed Function 1
public int calculatePrintedLength(ReadablePeriod period, Locale locale) {
    long valueLong = getFieldValue(period);
    if (valueLong == Long.MAX_VALUE) {
        return 0;
    }
    // use DecimalFormat to get the correct length for the value
    DecimalFormat decimalFormat = new DecimalFormat();
    decimalFormat.setGroupingUsed(false);
    decimalFormat.setDecimalSeparatorAlwaysShown(false);
    decimalFormat.setMinimumFractionDigits(0);
    decimalFormat.setMaximumFractionDigits(0);
    decimalFormat.setMinimumIntegerDigits(iMinPrintedDigits);
    String valueString = decimalFormat.format(valueLong);
    int sum = valueString.length();

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
        // reset valueLong to refer to the seconds part for the prefic/suffix calculation
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
    // use DecimalFormat to print the value correctly
    DecimalFormat decimalFormat = new DecimalFormat();
    decimalFormat.setGroupingUsed(false);
    decimalFormat.setDecimalSeparatorAlwaysShown(false);
    decimalFormat.setMinimumFractionDigits(0);
    decimalFormat.setMaximumFractionDigits(0);
    decimalFormat.setMinimumIntegerDigits(iMinPrintedDigits);
    String valueString = decimalFormat.format(valueLong);
    int value = Integer.parseInt(valueString);

    if (iFieldType >= SECONDS_MILLIS) {
        value = (int) (valueLong / DateTimeConstants.MILLIS_PER_SECOND);
    }
    if (iPrefix != null) {
        iPrefix.printTo(buf, value);
    }
    buf.append(valueString); // print the valueString instead of formatting it again
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