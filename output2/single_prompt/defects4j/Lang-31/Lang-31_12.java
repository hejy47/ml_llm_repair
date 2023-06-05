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
                char ch2 = cs.charAt(i + 1);
                if (Character.isLowSurrogate(ch2)) {
                    int codePoint = Character.toCodePoint(ch, ch2);
                    if (contains(searchChars, codePoint)) {
                        return true;
                    }
                }
            }
            // Skip over this supplementary character
            i++;
        } else {
            // ch is in the Basic Multilingual Plane
            if (contains(searchChars, ch)) {
                return true;
            }
        }
    }
    return false;
}

// Helper function to check if an array contains a given value
private static boolean contains(char[] array, int value) {
    for (char element : array) {
        if (element == value) {
            return true;
        }
    }
    return false;
}