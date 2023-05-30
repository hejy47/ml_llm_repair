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
            if (i + 1 < csLength) {
                char ch2 = cs.charAt(i+1);
                if (Character.isLowSurrogate(ch2)) {
                    int codePoint = Character.toCodePoint(ch, ch2);
                    if (ArrayUtils.contains(searchChars, Character.highSurrogate(codePoint)) ||
                            ArrayUtils.contains(searchChars, Character.lowSurrogate(codePoint))) {
                        return true;
                    }
                }
            }
            i++; //skip the supplementary character
        } else {
            // ch is in the Basic Multilingual Plane
            if (ArrayUtils.contains(searchChars, ch)) {
                return true;
            }
        }
    }
    return false;
}