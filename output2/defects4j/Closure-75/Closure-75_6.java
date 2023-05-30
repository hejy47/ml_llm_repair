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
        return null;
    }
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
        case 0x0009:
        // <LF>
        case 0x000A:
        // <VT>
        case 0x000B:
        // <FF>
        case 0x000C:
        // <CR>
        case 0x000D:
        // <SP>
        case 0x0020:
        // <NBSP>
        case 0x00A0:
        // <BOM>
        case 0xFEFF:
        // <ZWNBSP>
        case 0xFEFF:
        // <USP>
        case 0x2000:
        case 0x2001:
        case 0x2002:
        case 0x2003:
        case 0x2004:
        case 0x2005:
        case 0x2006:
        case 0x2007:
        case 0x2008:
        case 0x2009:
        case 0x200A:
        case 0x202F:
        case 0x205F:
        // <NEL>
        case 0x0085:
        // <LS>
        case 0x2028:
        // <PS>
        case 0x2029:
            return TernaryValue.TRUE;
        default:
            return (Character.getType(c) == Character.SPACE_SEPARATOR) ? TernaryValue.TRUE : TernaryValue.FALSE;
    }
}