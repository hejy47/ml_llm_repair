## Fixed Function 1
static Double getStringNumberValue(String rawJsString) {
    // vertical tab is not always whitespace
    String s = trimJsWhiteSpace(rawJsString);
    // return ScriptRuntime.toNumber(s);
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
        return Double.NaN;
    }
    // FireFox and IE treat the "Infinity" differently. FireFox is case
    // insensitive, but IE treats "infinity" as NaN.  So leave it alone.
    if (s.equalsIgnoreCase("infinity") || s.equalsIgnoreCase("-infinity") || s.equalsIgnoreCase("+infinity")) {
        return Double.POSITIVE_INFINITY;
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
        case // <VT>
        '\u000B':
            return TernaryValue.TRUE;
        // <SP>
        case ' ':
        // <LF>
        case '\n':
        // <CR>
        case '\r':
        // <TAB>
        case '\t':
        // <NBSP>
        case '\u00A0':
        // <FF>
        case '\u000C':
        // <LS>
        case '\u2028':
        // <PS>
        case '\u2029':
        case // <BOM>
        '\uFEFF':
            return TernaryValue.TRUE;
        default:
            int type = Character.getType(c);
            return (type == Character.SPACE_SEPARATOR || type == Character.LINE_SEPARATOR || type == Character.PARAGRAPH_SEPARATOR) ? TernaryValue.TRUE : TernaryValue.FALSE;
    }
}