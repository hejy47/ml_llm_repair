## Fixed Function 1
static Double getStringNumberValue(String rawJsString) {
    // vertical tab is not always whitespace
    String s = trimJsWhiteSpace(rawJsString);
    if (s.length() == 0) {
        return 0.0;
    }
    if (s.length() > 2 && s.charAt(0) == '0' && (s.charAt(1) == 'x' || s.charAt(1) == 'X')) {
        // Attempt to convert hex numbers.
        try {
            return Double.valueOf(Integer.parseInt(s.substring(2), 16));
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }
    if (s.length() > 3 && (s.charAt(0) == '-' || s.charAt(0) == '+') && s.charAt(1) == '0' && (s.charAt(2) == 'x' || s.charAt(2) == 'X')) {
        // hex numbers with explicit signs vary between browsers.
        return null;
    }
    // FireFox and IE treat the "Infinity" differently. FireFox is case
    // insensitive, but IE treats "infinity" as NaN.  So leave it alone.
    if (s.equalsIgnoreCase("infinity") || s.equalsIgnoreCase("-infinity") || s.equalsIgnoreCase("+infinity")) {
        return null;
    }
    try {
        return Double.parseDouble(s);
    } catch (NumberFormatException e) {
        return Double.NaN;
    }
}

## Fixed Function 2
static TernaryValue isStrWhiteSpaceChar(int c) {
    switch(c) {
        case '\u0009': // <TAB> was corrected to this unicode value
        case ' ':
        case '\n':
        case '\r':
        case '\u00A0':
        case '\u000C':
        case '\u2028':
        case '\u2029':
        case '\uFEFF':
            return TernaryValue.TRUE;
        default:
            return (Character.getType(c) == Character.SPACE_SEPARATOR) ? TernaryValue.TRUE : TernaryValue.FALSE;
    }
}