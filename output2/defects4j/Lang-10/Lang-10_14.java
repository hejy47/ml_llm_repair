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
                break;
        }
        if (REGEX_SPECIAL_CHARACTERS.contains(String.valueOf(c))) {
            regex.append('\\');
        }
        regex.append(c);
    }
    return regex;
} 

//Add a Set for special characters that need to be escaped in regex