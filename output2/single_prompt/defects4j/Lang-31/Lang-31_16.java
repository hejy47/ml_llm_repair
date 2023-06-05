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
        for (int j = 0; j < searchLength; j++) {
            if (Character.charCount(ch) == 2) {
                // ch is a supplementary character
                if (searchChars[j] == ch && j < searchLength - 1 && Character.isSurrogatePair(ch, searchChars[j + 1])) {
                    // ch and the next character form a surrogate pair
                    return true;
                }
            } else {
                // ch is in the Basic Multilingual Plane
                if (searchChars[j] == ch) {
                    return true;
                }
            }
        }
    }
    return false;
}