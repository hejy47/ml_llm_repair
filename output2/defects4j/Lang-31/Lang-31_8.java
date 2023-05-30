## Fixed Function 1
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
            // ch is a supplementary character
            if (i < csLength - 1) {
                char ch2 = cs.charAt(i + 1);
                if (Character.isLowSurrogate(ch2)) {
                    int codePoint = Character.toCodePoint(ch, ch2);
                    for (int j = 0; j < searchLength; j++) {
                        if (codePoint == searchChars[j]) {
                            return true;
                        }
                    }
                }
            }
            i++; //skip the low surrogate we just processed
        } else {
            // ch is in the Basic Multilingual Plane
            for (int j = 0; j < searchLength; j++) {
                if (searchChars[j] == ch) {
                    return true;
                }
            }
        }
    }
    return false;
}