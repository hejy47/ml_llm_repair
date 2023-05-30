## Fixed Function 1
private StringBuffer appendQuotedString(String pattern, ParsePosition pos, StringBuffer appendTo, boolean escapingOn) {
    int start = pos.getIndex();
    char[] c = pattern.toCharArray();
    if (escapingOn && c[start] == QUOTE) {
        return appendTo == null ? null : appendTo.append(QUOTE);
    }
    int lastHold = start;
    for (int i = pos.getIndex(); i < pattern.length(); i++) {
        if (escapingOn && pattern.substring(i).startsWith(ESCAPED_QUOTE)) {
            appendTo.append(c, lastHold, pos.getIndex() - lastHold).append(QUOTE);
            pos.setIndex(i + ESCAPED_QUOTE.length());
            lastHold = pos.getIndex();
            continue;
        }
        switch(c[pos.getIndex()]) {
            case QUOTE:
                next(pos);
                return appendTo == null ? null : appendTo.append(c, lastHold, pos.getIndex() - lastHold);
            
            // add a case for the ESCAPED_QUOTE character
            case ESCAPED_QUOTE:
                if (!escapingOn) {
                    appendTo.append(c, lastHold, pos.getIndex() - lastHold).append(ESCAPED_QUOTE);
                    pos.setIndex(pos.getIndex() + ESCAPED_QUOTE.length());
                    lastHold = pos.getIndex();
                }
                break;
                
            default:
                next(pos);
        }
    }
    throw new IllegalArgumentException("Unterminated quoted string at position " + start);
}