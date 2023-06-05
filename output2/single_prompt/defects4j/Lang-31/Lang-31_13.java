## Fixed Function 1 (assuming the intended behavior is to check for any character in the searchChars array, including supplementary characters)
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
        if (Character.charCount(ch) > 1) {
            // ch is a supplementary character
            if (contains(searchChars, ch)) {
                return true;
            }
            i++; //skip the next char, since it's a part of the supplementary character
        } else {
            // ch is in the Basic Multilingual Plane
            if (contains(searchChars, ch)) {
                return true;
            }
        }
    }
    return false;
}

// Helper method to check if a character is in an array
private static boolean contains(char[] array, char ch) {
    for (char c : array) {
        if (c == ch) {
            return true;
        }
    }
    return false;
}