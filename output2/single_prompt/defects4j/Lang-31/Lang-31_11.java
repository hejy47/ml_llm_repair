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
            if (i < csLength - 1) {
                i++;
                char ch2 = cs.charAt(i);
                if (Character.isLowSurrogate(ch2)) {
                    ch = Character.toCodePoint(ch, ch2);
                }
            } else {
                // invalid high surrogate
                break;
            }
        }
        for (int j = 0; j < searchLength; j++) {
            if (searchChars[j] == ch) {
                return true;
            }
        }
    }
    return false;
}