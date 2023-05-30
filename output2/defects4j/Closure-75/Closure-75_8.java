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
        } catch (NullPointerException e) { // add this catch for cases where s is null
            return Double.NaN;
        }
    }
    if (s.length() > 3 && (s.charAt(0) == '-' || s.charAt(0) == '+') && s.charAt(1) == '0' && (s.charAt(2) == 'x' || s.charAt(2) == 'X')) {
        // hex numbers with explicit signs vary between browsers.
        return null;
    }
    // FireFox and IE treat the "Infinity" differently. FireFox is case
    // insensitive, but IE treats "infinity" as NaN.  So leave it alone.
    if (s.equals("infinity") || s.equals("-infinity") || s.equals("+infinity")) {
        return null;
    }
    try {
        return Double.parseDouble(s);
    } catch (NumberFormatException e) {
        return Double.NaN;
    } catch (NullPointerException e) { // add this catch for cases where s is null
        return Double.NaN;
    }
}

## Fixed Function 2
static TernaryValue isStrWhiteSpaceChar(int c) {
    switch(c) {
        case '\u0009': // <TAB>
        case '\u000B': // <VT>
        case '\u000C': // <FF>
        case '\u0020': // <SP>
        case '\u00A0': // <NBSP>
        case '\u1680': // <OGHAM SPACE MARK>
        case '\u180E': // <MONGOLIAN VOWEL SEPARATOR>
        case '\u2000': // <EN QUAD>
        case '\u2001': // <EM QUAD>
        case '\u2002': // <EN SPACE>
        case '\u2003': // <EM SPACE>
        case '\u2004': // <THREE-PER-EM SPACE>
        case '\u2005': // <FOUR-PER-EM SPACE>
        case '\u2006': // <SIX-PER-EM SPACE>
        case '\u2007': // <FIGURE SPACE>
        case '\u2008': // <PUNCTUATION SPACE>
        case '\u2009': // <THIN SPACE>
        case '\u200A': // <HAIR SPACE>
        case '\u202F': // <NARROW NO-BREAK SPACE>
        case '\u205F': // <MEDIUM MATHEMATICAL SPACE>
        case '\u3000': // <IDEOGRAPHIC SPACE>
            return TernaryValue.TRUE;
        case '\u000A': // <LF>
        case '\u000D': // <CR>
        case '\u2028': // <LS>
        case '\u2029': // <PS>
            return TernaryValue.LINEBREAK;
        default:
            return (Character.getType(c) == Character.SPACE_SEPARATOR) ? TernaryValue.TRUE : TernaryValue.FALSE;
    }
}