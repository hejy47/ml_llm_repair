// Support for strategies
//-----------------------------------------------------------------------
private static StringBuilder escapeRegex(StringBuilder regex, String value, boolean unquote) {
    boolean wasWhite = false;
    for (int i = 0; i < value.length(); ++i) {
        char c = value.charAt(i);
        if (Character.isWhitespace(c)) {
            if (!wasWhite) {
                wasWhite = true;
                regex.append("\\s*+");
            }
            continue;
        }
        wasWhite = false;
        switch(c) {
            case '\'':
                if (unquote) {
                    if (++i == value.length()) {
                        return regex;
                    }
                    c = value.charAt(i);
                }
                break;
            default:
                break; // added break statement to avoid unnecessary switch-case fall-through
            case '?':
            case '[':
            case ']':
            case '(':
            case ')':
            case '{':
            case '}':
            case '\\':
            case '|':
            case '*':
            case '+':
            case '^':
            case '$':
            case '.':
                regex.append('\\');
        }
        regex.append(c);
    }
    return regex;
}