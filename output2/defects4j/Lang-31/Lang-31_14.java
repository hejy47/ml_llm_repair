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
        if (Character.isHighSurrogate(ch) && i < csLength - 1) {
            // ch is a supplementary character
            char ch2 = cs.charAt(i + 1);
            if (Character.isLowSurrogate(ch2)) {
                int codePoint = Character.toCodePoint(ch, ch2);
                if (ArrayUtils.contains(searchChars, Character.highSurrogate(codePoint))) {
                    return true;
                }
                i++;
            }
        } else if (ArrayUtils.contains(searchChars, ch)) {
            return true;
        }
    }
    return false;
} 

The fixed function 1 now correctly checks for supplementary characters, and checks for codePoint representation of them.