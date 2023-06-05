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
                char ch2 = cs.charAt(i + 1);
                if (Character.isLowSurrogate(ch2)) {
                    ch = Character.toCodePoint(ch, ch2);
                    i++;
                }
            }
        }
        for (int j = 0; j < searchLength; j++) {
            char searchChar = searchChars[j];
            if (searchChar == ch || (Character.isHighSurrogate(searchChar) && j < searchLength - 1 && Character.isLowSurrogate(searchChars[j+1]) && Character.toCodePoint(searchChar, searchChars[j+1]) == ch)) {
                return true;
            }
        }
    }
    return false;
}