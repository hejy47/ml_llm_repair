// ContainsAny
//-----------------------------------------------------------------------
public static boolean containsAny(CharSequence cs, char[] searchChars) {
    if (isEmpty(cs) || ArrayUtils.isEmpty(searchChars)) {
        return false;
    }
    int csLength = cs.length();
    int searchLength = searchChars.length;
    for (int i = 0; i < csLength; i++) {
        char ch = cs.charAt(i);
        if (Character.isHighSurrogate(ch)) {
            if (i + 1 < csLength) {
                // Get the next char and create
                // the supplementary character
                char next = cs.charAt(i + 1);
                int codePoint = Character.toCodePoint(ch, next);
                for (int j = 0; j < searchLength; j++) {
                    if (searchChars[j] == ch) {
                        return true;
                    }
                }
                // Move to the next index since
                // the supplementary character takes
                // 2 char values
                i++;
            }
        } else {
            for (int j = 0; j < searchLength; j++) {
                if (searchChars[j] == ch) {
                    return true;
                }
            }
        }
    }
    return false;
}