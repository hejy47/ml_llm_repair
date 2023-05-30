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
        if (Character.isSurrogate(ch)) { // check if ch is a supplementary character
            if (i < csLength - 1) { // check if there is another char to form a supplementary char
                char ch2 = cs.charAt(i + 1);
                if (Character.isSurrogatePair(ch, ch2)) { // check if the char is a valid supplementary char
                    i++; // move to the next char to avoid counting it twice
                    continue;
                }
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