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
        int ch = Character.codePointAt(cs, i);
        if (Character.isSupplementaryCodePoint(ch)) {
            i++; //increment the index as supplementary characters use 2 chars
        }
        for (int j = 0; j < searchLength; j++) {
            if (searchChars[j] == ch) {
                return true;
            }
        }
    }
    return false;
}