## Fixed Function 1
static Double getStringNumberValue(String rawJsString) {
    String s = trimJsWhiteSpace(rawJsString);
    if (s.length() == 0) {
        return 0.0;
    }
    try {
        return Double.parseDouble(s);
    } catch (NumberFormatException e) {
        if (s.length() > 2 && s.charAt(0) == '0' && (s.charAt(1) == 'x' || s.charAt(1) == 'X')) {
            try {
                return Double.valueOf(Integer.parseInt(s.substring(2), 16));
            } catch (NumberFormatException ex) {
                return Double.NaN;
            }
        } else if (s.length() > 3 && (s.charAt(0) == '-' || s.charAt(0) == '+') && s.charAt(1) == '0' && (s.charAt(2) == 'x' || s.charAt(2) == 'X')) {
            return null;
        } else if (s.equalsIgnoreCase("infinity") || s.equalsIgnoreCase("-infinity") || s.equalsIgnoreCase("+infinity")) {
            return null;
        }
        return Double.NaN;
    }
}

## Fixed Function 2
static TernaryValue isStrWhiteSpaceChar(int c) {
    switch(c) {
        case // <VT>
        '\u000B':
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
            return (Character.getType(c) == Character.SPACE_SEPARATOR || Character.isWhitespace(c)) ? TernaryValue.TRUE : TernaryValue.FALSE;
    }
}