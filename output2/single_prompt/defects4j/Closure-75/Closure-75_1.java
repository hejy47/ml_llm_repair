## Fixed Function 1
static Double getStringNumberValue(String rawJsString) {
    String s = trimJsWhiteSpace(rawJsString);
    if (s.length() == 0) {
        return 0.0;
    }
    if (s.length() > 2 && s.charAt(0) == '0' && (s.charAt(1) == 'x' || s.charAt(1) == 'X')) {
        try {
            return Double.valueOf(Integer.parseInt(s.substring(2), 16));
        } catch (NumberFormatException e) {
            return Double.NaN;
        }
    }
    if (s.length() > 3 && (s.charAt(0) == '-' || s.charAt(0) == '+') && s.charAt(1) == '0' && (s.charAt(2) == 'x' || s.charAt(2) == 'X')) {
        return Double.NaN; // Return NaN instead of null
    }
    if (s.equalsIgnoreCase("infinity") || s.equalsIgnoreCase("-infinity") || s.equalsIgnoreCase("+infinity")) { // Use equalsIgnoreCase to compare strings
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
        case 0x0009: // Horizontal tab
        case 0x000A: // Line feed
        case 0x000B: // Vertical tab
        case 0x000C: // Form feed
        case 0x000D: // Carriage return
        case 0x0020: // Space
        case 0x0085: // Next line
        case 0x00A0: // No-break space
        case 0x1680: // Ogham space mark
        case 0x2000: // En quad
        case 0x2001: // Em quad
        case 0x2002: // En space
        case 0x2003: // Em space
        case 0x2004: // Three-per-em space
        case 0x2005: // Four-per-em space
        case 0x2006: // Six-per-em space
        case 0x2007: // Figure space
        case 0x2008: // Punctuation space
        case 0x2009: // Thin space
        case 0x200A: // Hair space
        case 0x2028: // Line separator
        case 0x2029: // Paragraph separator
        case 0x202F: // Narrow no-break space
        case 0x205F: // Medium mathematical space
        case 0x3000: // Ideographic space
            return TernaryValue.TRUE;
        default:
            return (Character.getType(c) == Character.SPACE_SEPARATOR) ? TernaryValue.TRUE : TernaryValue.FALSE;
    }
}