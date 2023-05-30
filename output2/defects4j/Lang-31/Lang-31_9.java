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
                    // ch and ch2 form a surrogate pair
                    int codePoint = Character.toCodePoint(ch, ch2);
                    if (contains(searchChars, codePoint)) {
                        return true;
                    }
                }
            }
            i++; // skip the second surrogate of the pair
        } else {
            // ch is in the Basic Multilingual Plane
            if (contains(searchChars, ch)) {
                return true;
            }
        }
    }
    return false;
}

// Helper function to check if an array contains a specific value
private static boolean contains(char[] arr, int value) {
    for (char c : arr) {
        if (c == value) {
            return true;
        }
    }
    return false;
}